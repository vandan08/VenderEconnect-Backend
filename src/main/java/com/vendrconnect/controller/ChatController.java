package com.vendrconnect.controller;

import com.vendrconnect.dto.ChatRequest;
import com.vendrconnect.dto.ChatResponse;
import com.vendrconnect.model.ChatMessage;
import com.vendrconnect.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
public class ChatController {

    @Autowired
    private ChatbotService chatbotService;

    @MessageMapping("/message")
    @SendTo("/topic/chat")
    public ChatResponse handleChatMessage(ChatRequest chatRequest) {
        String response = chatbotService.generateResponse(chatRequest.getMessage(), chatRequest.getUserId());
        return new ChatResponse(chatRequest.getMessage(), response, chatRequest.getUserId());
    }

    @PostMapping("/message")
    public ResponseEntity<ChatResponse> sendMessage(@RequestBody ChatRequest chatRequest) {
        String response = chatbotService.generateResponse(chatRequest.getMessage(), chatRequest.getUserId());
        ChatResponse chatResponse = new ChatResponse(chatRequest.getMessage(), response, chatRequest.getUserId());
        return ResponseEntity.ok(chatResponse);
    }

    @GetMapping("/history/{userId}")
    public ResponseEntity<List<ChatMessage>> getChatHistory(@PathVariable String userId) {
        List<ChatMessage> history = chatbotService.getChatHistory(userId);
        return ResponseEntity.ok(history);
    }
}