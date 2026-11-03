package com.portfolio.aiservice.rag;

import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Service;

@Service
public class DocumentChunkServiceImpl
        implements DocumentChunkService {

    private final TokenTextSplitter textSplitter =
            TokenTextSplitter.builder().build();

    @Override
    public List<Document> createChunks(
            Long documentId,
            String filename,
            String extractedText) {

        if (extractedText == null || extractedText.isBlank()) {
            throw new IllegalArgumentException(
                    "Document does not contain extracted text"
            );
        }

        Document document = new Document(
                extractedText,
                Map.of(
                        "documentId", documentId.toString(),
                        "filename", filename
                )
        );

        return textSplitter.apply(List.of(document));
    }
}