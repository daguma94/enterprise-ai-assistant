package com.portfolio.aiservice.service;

import com.portfolio.aiservice.client.DocumentClient;

import com.portfolio.aiservice.dto.AiResponse;
import com.portfolio.aiservice.rag.DocumentSearchService;

import org.junit.jupiter.api.Test;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.ChatClient.CallResponseSpec;
import org.springframework.ai.chat.client.ChatClient.ChatClientRequestSpec;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

import java.util.List;

import org.springframework.ai.document.Document;

class AiServiceImplTest {

    @Test
    void shouldAnswerQuestion() {

        ChatClient chatClient = mock(ChatClient.class);
        ChatClientRequestSpec requestSpec =
                mock(ChatClientRequestSpec.class);
        CallResponseSpec responseSpec =
                mock(CallResponseSpec.class);

        DocumentClient documentClient = mock(DocumentClient.class);

        DocumentSearchService documentSearchService =
        mock(DocumentSearchService.class);

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);
        when(responseSpec.content())
                .thenReturn("Spring Boot es un framework de Java.");

        AiServiceImpl aiService = new AiServiceImpl(
        chatClient,
        documentClient,
        documentSearchService
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
        Long documentId = 6L;
        String question = "¿Qué son los virtual threads?";

        DocumentSearchService documentSearchService =
                mock(DocumentSearchService.class);

        DocumentClient documentClient =
                mock(DocumentClient.class);

        ChatClient chatClient = mock(ChatClient.class);

        ChatClientRequestSpec requestSpec =
                mock(ChatClientRequestSpec.class);

        CallResponseSpec responseSpec =
                mock(CallResponseSpec.class);

        Document chunk = new Document(
                "Los virtual threads de Java 21 permiten "
                + "escalar tareas que esperan operaciones de E/S."
        );

        when(documentSearchService.searchRelevantChunks(
                documentId, question))
                .thenReturn(List.of(chunk));

        when(chatClient.prompt()).thenReturn(requestSpec);
        when(requestSpec.system(anyString())).thenReturn(requestSpec);
        when(requestSpec.user(anyString())).thenReturn(requestSpec);
        when(requestSpec.call()).thenReturn(responseSpec);

        when(responseSpec.content())
                .thenReturn(
                        "Los virtual threads permiten gestionar "
                        + "muchas tareas concurrentes de E/S."
                );

        AiServiceImpl aiService = new AiServiceImpl(
                chatClient,
                documentClient,
                documentSearchService
        );

        // Act
        AiResponse response =
                aiService.askDocument(documentId, question);

        // Assert
        assertEquals(
                "Los virtual threads permiten gestionar "
                + "muchas tareas concurrentes de E/S.",
                response.answer()
        );

        verify(documentSearchService)
                .searchRelevantChunks(documentId, question);

        verify(requestSpec).user(
                contains("Los virtual threads de Java 21")
        );

        verify(requestSpec).user(
                contains(question)
        );
        }
}