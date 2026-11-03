package com.portfolio.aiservice.rag;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import com.portfolio.aiservice.client.DocumentClient;
import com.portfolio.aiservice.client.dto.DocumentContentResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentEmbeddingServiceImpl
        implements DocumentEmbeddingService {

    private final DocumentClient documentClient;
    private final DocumentChunkService documentChunkService;
    private final VectorStore vectorStore;

    @Override
    public void indexDocument(Long documentId) {

        // 1. Recuperar el contenido del PDF
        DocumentContentResponse document =
                documentClient.getDocumentContent(documentId);

        // 2. Dividir el texto en fragmentos
        List<Document> chunks =
                documentChunkService.createChunks(
                        document.id(),
                        document.filename(),
                        document.extractedText()
                );

        // 3. Generar embeddings y almacenarlos
        vectorStore.add(chunks);
    }
}