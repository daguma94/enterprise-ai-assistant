package com.portfolio.aiservice.rag;

import java.util.List;

import org.springframework.ai.document.Document;

public interface DocumentSearchService {

    List<Document> searchRelevantChunks(
            Long documentId,
            String question
    );
}