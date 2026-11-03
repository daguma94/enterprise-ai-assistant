package com.portfolio.aiservice.rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentSearchServiceImpl
        implements DocumentSearchService {

    private final VectorStore vectorStore;

    @Override
    public List<Document> searchRelevantChunks(
            Long documentId,
            String question) {

        SearchRequest request = SearchRequest.builder()
                .query(question)
                .topK(4)
                .similarityThreshold(0.5)
                .filterExpression(
                        "documentId == '" + documentId + "'"
                )
                .build();

        return vectorStore.similaritySearch(request);
    }
}