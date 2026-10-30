package com.portfolio.aiservice.service;

import com.portfolio.aiservice.client.DocumentClient;
import com.portfolio.aiservice.client.dto.DocumentContentResponse;
import com.portfolio.aiservice.dto.AiResponse;

import org.junit.jupiter.api.Test;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

class AiServiceImplTest {

    @Test
    void shouldAnswerQuestion() {

        ChatClient chatClient = mock(ChatClient.class);
        ChatClientRequestSpec requestSpec =
                mock(ChatClientRequestSpec.class);
        CallResponseSpec responseSpec =
                mock(CallResponseSpec.class);

        DocumentClient documentClient = mock(DocumentClient.class);

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content())
                .thenReturn("Spring Boot es un framework de Java.");

        AiServiceImpl aiService = new AiServiceImpl(
        chatClient,
        documentClient
        );

        AiResponse response =
                aiService.askQuestion("¿Qué es Spring Boot?");

        assertEquals(
                "Spring Boot es un framework de Java.",
                response.answer()
        );

        verify(requestSpec).user("¿Qué es Spring Boot?");
    }

    @Test
    void shouldAnswerQuestionAboutDocument() {

        // Arrange
        Long documentId = 1L;
        String question = "¿Qué es Spring Boot?";

        DocumentContentResponse document =
                new DocumentContentResponse(
                        documentId,
                        "spring-boot.pdf",
                        "Spring Boot simplifies the development of Java applications."
                );

        DocumentClient documentClient = mock(DocumentClient.class);
        ChatClient chatClient = mock(ChatClient.class);

        ChatClientRequestSpec requestSpec =
                mock(ChatClientRequestSpec.class);

        CallResponseSpec responseSpec =
                mock(CallResponseSpec.class);

        when(documentClient.getDocumentContent(documentId))
                .thenReturn(document);

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);

        when(responseSpec.content())
                .thenReturn("Spring Boot simplifica el desarrollo de aplicaciones Java.");

        AiServiceImpl aiService =
                new AiServiceImpl(chatClient, documentClient);

        // Act
        AiResponse response =
                aiService.askDocument(documentId, question);

        // Assert
        assertEquals(
                "Spring Boot simplifica el desarrollo de aplicaciones Java.",
                response.answer()
        );

        verify(documentClient).getDocumentContent(documentId);

        verify(requestSpec).user(
                contains("Spring Boot simplifies the development of Java applications.")
        );

        verify(requestSpec).user(
                contains(question)
        );
    }
}