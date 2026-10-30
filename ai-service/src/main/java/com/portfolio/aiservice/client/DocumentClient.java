package com.portfolio.aiservice.client;

import com.portfolio.aiservice.client.dto.DocumentContentResponse;

public interface DocumentClient {

    DocumentContentResponse getDocumentContent(Long documentId);

}