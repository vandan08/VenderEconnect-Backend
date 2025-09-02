package com.vendrconnect.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vendrconnect.repository.ChatMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Service
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);
    private final WebClient webClient;
    private final ChatMessageRepository chatMessageRepository;
    private final ObjectMapper objectMapper;
    private final String apiKey;

    public ChatbotService(@Value("${chatbot.gemini.api-key}") String apiKey,
                         ChatMessageRepository chatMessageRepository) {
        this.apiKey = apiKey;
        this.chatMessageRepository = chatMessageRepository;
        this.objectMapper = new ObjectMapper();
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        logger.info("ChatbotService initialized with API key: {}...", 
                   apiKey != null && apiKey.length() > 10 ? apiKey.substring(0, 10) : "INVALID");
    }

    public String generateResponse(String userMessage, String userId) {
        try {
            logger.info("Processing message from user {}: {}", userId, userMessage);
            
            String systemPrompt = "You are a helpful assistant for VendrConnect, a service marketplace platform. " +
                    "Help users with questions about finding services, posting jobs, managing their profiles, " +
                    "and using the platform. Keep responses concise and helpful.\n\n";

            String fullPrompt = systemPrompt + "User: " + userMessage;
            
            Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                    Map.of("parts", new Object[]{
                        Map.of("text", fullPrompt)
                    })
                }
            );

            logger.info("Sending request to Gemini API...");
            String response = webClient.post()
                    .uri("/v1beta/models/gemini-2.5-pro:generateContent?key=" + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            logger.info("Received response from Gemini: {}", response);
            String aiResponse = extractTextFromResponse(response);
            logger.info("Extracted AI response: {}", aiResponse);
            
            saveChatMessage(userId, userMessage, aiResponse);
            return aiResponse;
            
        } catch (WebClientResponseException e) {
            logger.error("WebClient error: Status={}, Body={}", e.getStatusCode(), e.getResponseBodyAsString());
            return "API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            logger.error("Error generating response", e);
            return "Error: " + e.getMessage();
        }
    }

    private String extractTextFromResponse(String response) {
        try {
            JsonNode jsonNode = objectMapper.readTree(response);
            logger.info("Parsing JSON response: {}", jsonNode.toString());
            
            JsonNode candidates = jsonNode.path("candidates");
            if (candidates.isEmpty()) {
                logger.error("No candidates found in response");
                return "No response generated";
            }
            
            JsonNode content = candidates.get(0).path("content");
            JsonNode parts = content.path("parts");
            
            if (parts.isEmpty()) {
                logger.error("No parts found in response");
                return "Empty response from AI";
            }
            
            String text = parts.get(0).path("text").asText();
            logger.info("Successfully extracted text: {}", text);
            return text;
            
        } catch (Exception e) {
            logger.error("Error parsing response: {}", e.getMessage());
            return "Parse error: " + e.getMessage();
        }
    }

    private void saveChatMessage(String userId, String message, String response) {
        com.vendrconnect.model.ChatMessage chatMessage = new com.vendrconnect.model.ChatMessage();
        chatMessage.setUserId(userId);
        chatMessage.setMessage(message);
        chatMessage.setResponse(response);
        chatMessageRepository.save(chatMessage);
    }

    public java.util.List<com.vendrconnect.model.ChatMessage> getChatHistory(String userId) {
        return chatMessageRepository.findTop10ByUserIdOrderByTimestampDesc(userId);
    }
}