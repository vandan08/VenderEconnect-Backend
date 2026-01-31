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

/**
 * Service class responsible for handling chatbot interactions using Google's Gemini AI.
 * This service manages the communication between users and the AI assistant,
 * including generating responses, saving chat history, and retrieving conversation logs.
 * 
 * @author VenderConnect Team
 * @version 1.0
 */
@Service
public class ChatbotService {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotService.class);
    
    /**
     * WebClient for making HTTP requests to Gemini API
     * Uses reactive programming model for non-blocking I/O operations
     */
    private final WebClient webClient;
    
    /**
     * Repository for persisting chat messages to database
     * Enables retrieval of conversation history for context
     */
    private final ChatMessageRepository chatMessageRepository;
    
    /**
     * ObjectMapper for JSON serialization/deserialization
     * Used to parse API responses and construct request bodies
     */
    private final ObjectMapper objectMapper;
    
    /**
     * API key for Google Gemini AI service
     * Loaded from environment variable for security
     */
    private final String apiKey;
    
    /**
     * Knowledge base service for RAG (Retrieval-Augmented Generation).
     * Provides relevant context from system documentation for better AI responses.
     */
    private final KnowledgeBaseService knowledgeBaseService;

    /**
     * Constructor-based dependency injection for ChatbotService.
     * Initializes WebClient with base URL and default headers.
     * 
     * @param apiKey Gemini API key from application configuration
     * @param chatMessageRepository Repository for chat message persistence
     * @param knowledgeBaseService RAG knowledge base for context retrieval
     */
    public ChatbotService(@Value("${chatbot.gemini.api-key}") String apiKey,
                         ChatMessageRepository chatMessageRepository,
                         KnowledgeBaseService knowledgeBaseService) {
        this.apiKey = apiKey;
        this.chatMessageRepository = chatMessageRepository;
        this.knowledgeBaseService = knowledgeBaseService;
        this.objectMapper = new ObjectMapper();
        
        /**
         * Configure WebClient with base URL for Gemini API
         * Sets default content type header to JSON
         */
        this.webClient = WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        // Log initialization with masked API key for security
        logger.info("ChatbotService initialized with API key: {}...", 
                   apiKey != null && apiKey.length() > 10 ? apiKey.substring(0, 10) : "INVALID");
    }

    /**
     * Generates a response from the AI based on user's message.
     * This method constructs a prompt with system instructions, sends it to Gemini API,
     * extracts the response, and saves the conversation to the database.
     * 
     * @param userMessage The message sent by the user
     * @param userId Unique identifier of the user for conversation tracking
     * @return AI-generated response as a string
     */
    public String generateResponse(String userMessage, String userId) {
        try {
            logger.info("Processing message from user {}: {}", userId, userMessage);
            
            /**
             * Retrieve relevant knowledge from RAG system to provide context.
             * This enables AI to answer questions using system documentation.
             */
            String ragContext = knowledgeBaseService.buildRagContext(userMessage);
            logger.info("RAG context retrieved: {} characters", ragContext.length());
            
            /**
             * System prompt defines the AI's persona and behavior
             * Provides context about VenderConnect platform and response style
             * Enhanced with RAG knowledge for more accurate responses
             */
            String systemPrompt = "You are a helpful assistant for VenderConnect, a service marketplace platform. " +
                    "Help users with questions about finding services, posting jobs, managing their profiles, " +
                    "and using of platform. Keep responses concise and helpful.\n\n";

            // Combine RAG context, system prompt, and user message for complete context
            String fullPrompt = systemPrompt + ragContext + "User Question: " + userMessage;
            
            /**
             * Construct request body in Gemini API format
             * Uses nested map structure for message contents
             */
            Map<String, Object> requestBody = Map.of(
                "contents", new Object[]{
                    Map.of("parts", new Object[]{
                        Map.of("text", fullPrompt)
                    })
                }
            );

            logger.info("Sending request to Gemini API...");
            
            /**
             * Make synchronous HTTP POST request to Gemini API
             * .block() converts reactive Mono to blocking result
             * Using gemini-2.5-pro model for advanced reasoning
             */
            String response = webClient.post()
                    .uri("/v1beta/models/gemini-2.5-pro:generateContent?key=" + apiKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            logger.info("Received response from Gemini: {}", response);
            
            // Extract the actual text response from JSON structure
            String aiResponse = extractTextFromResponse(response);
            logger.info("Extracted AI response: {}", aiResponse);
            
            // Persist the conversation for history tracking
            saveChatMessage(userId, userMessage, aiResponse);
            return aiResponse;
            
        } catch (WebClientResponseException e) {
            /**
             * Handle API-specific errors (4xx, 5xx status codes)
             * Log the status code and response body for debugging
             */
            logger.error("WebClient error: Status={}, Body={}", e.getStatusCode(), e.getResponseBodyAsString());
            return "API Error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
        } catch (Exception e) {
            /**
             * Handle unexpected exceptions (network issues, parsing errors, etc.)
             * Returns generic error message to user while logging details
             */
            logger.error("Error generating response", e);
            return "Error: " + e.getMessage();
        }
    }

    /**
     * Extracts the text content from Gemini API's JSON response.
     * The response structure is: { candidates: [{ content: { parts: [{ text: "..." }] }] }
     * 
     * @param response Raw JSON string from Gemini API
     * @return Extracted text content or error message if parsing fails
     */
    private String extractTextFromResponse(String response) {
        try {
            // Parse JSON string into JsonNode tree
            JsonNode jsonNode = objectMapper.readTree(response);
            logger.info("Parsing JSON response: {}", jsonNode.toString());
            
            /**
             * Navigate through the nested JSON structure:
             * root -> candidates[0] -> content -> parts[0] -> text
             * Using .path() instead of .get() to avoid NullPointerExceptions
             */
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
            
            // Extract the actual text content from the response
            String text = parts.get(0).path("text").asText();
            logger.info("Successfully extracted text: {}", text);
            return text;
            
        } catch (Exception e) {
            /**
             * Handle JSON parsing errors
             * Returns descriptive error message while logging stack trace
             */
            logger.error("Error parsing response: {}", e.getMessage());
            return "Parse error: " + e.getMessage();
        }
    }

    /**
     * Saves a chat message exchange to the database.
     * Creates a new ChatMessage entity and persists it using JPA repository.
     * Timestamp is automatically set by entity's constructor.
     * 
     * @param userId ID of the user who sent the message
     * @param message The user's original message
     * @param response The AI's response to the message
     */
    private void saveChatMessage(String userId, String message, String response) {
        com.vendrconnect.model.ChatMessage chatMessage = new com.vendrconnect.model.ChatMessage();
        chatMessage.setUserId(userId);
        chatMessage.setMessage(message);
        chatMessage.setResponse(response);
        chatMessageRepository.save(chatMessage);
    }

    /**
     * Retrieves the most recent chat history for a specific user.
     * Useful for providing context in future conversations or displaying chat UI.
     * 
     * @param userId ID of the user to fetch chat history for
     * @return List of the 10 most recent chat messages, ordered by newest first
     */
    public java.util.List<com.vendrconnect.model.ChatMessage> getChatHistory(String userId) {
        return chatMessageRepository.findTop10ByUserIdOrderByTimestampDesc(userId);
    }
}
