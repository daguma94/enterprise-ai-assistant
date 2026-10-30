package com.portfolio.aiservice.client;

import com.portfolio.aiservice.client.dto.DocumentContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class DocumentClientImpl implements DocumentClient {

    private final RestClient restClient;

    public DocumentClientImpl(
            RestClient.Builder restClientBuilder,
            @Value("${document.service.url}") String documentServiceUrl) {

        this.restClient = restClientBuilder
                .baseUrl(documentServiceUrl)
                .build();
    }

    @Override
    public DocumentContentResponse getDocumentContent(Long documentId) {

        return restClient.get()
                .uri("/documents/{id}/content", documentId)
                .retrieve()
                .body(DocumentContentResponse.class);
    }
}