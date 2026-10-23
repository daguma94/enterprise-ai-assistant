package com.portfolio.aiservice.service;

import com.portfolio.aiservice.dto.AiResponse;

import org.junit.jupiter.api.Test;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AiServiceImplTest {

    @Test
    void shouldAnswerQuestion() {

        ChatClient chatClient = mock(ChatClient.class);
        ChatClientRequestSpec requestSpec =
                mock(ChatClientRequestSpec.class);
        CallResponseSpec responseSpec =
                mock(CallResponseSpec.class);

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content())
                .thenReturn("Spring Boot es un framework de Java.");

        AiServiceImpl aiService = new AiServiceImpl(chatClient);

        AiResponse response =
                aiService.askQuestion("¿Qué es Spring Boot?");

        assertEquals(
                "Spring Boot es un framework de Java.",
                response.answer()
        );

        verify(requestSpec).user("¿Qué es Spring Boot?");
    }
}