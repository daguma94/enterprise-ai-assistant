package com.portfolio.aiservice.rag;

import java.util.List;

import org.springframework.ai.document.Document;

public interface DocumentChunkService {

    List<Document> createChunks(
            Long documentId,
            String filename,
            String extractedText
    );
}