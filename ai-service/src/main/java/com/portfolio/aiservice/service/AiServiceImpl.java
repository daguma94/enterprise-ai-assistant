package com.portfolio.aiservice.service;

import com.portfolio.aiservice.dto.AiResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    @Override
    public AiResponse askQuestion(String question) {

        String answer = chatClient.prompt()
                .user(question)
                .call()
                .content();

        return new AiResponse(answer);
    }
}