package com.portfolio.documentservice.service;

import com.portfolio.documentservice.dto.DocumentResponse;
import com.portfolio.documentservice.repository.DocumentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import com.portfolio.documentservice.entity.Document;

import java.nio.file.Files;

@ExtendWith(MockitoExtension.class)
class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    private DocumentServiceImpl documentService;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {

        documentService =
                new DocumentServiceImpl(documentRepository);

        ReflectionTestUtils.setField(
                documentService,
                "storageLocation",
                tempDir.toString()
        );
    }

    @Test
    void shouldUploadDocument() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "manual.pdf",
                "application/pdf",
                "%PDF-1.7\nTest PDF content"
                        .getBytes(StandardCharsets.US_ASCII)
        );

        when(documentRepository.save(any(Document.class)))
                .thenAnswer(invocation -> {
                    Document document = invocation.getArgument(0);
                    document.setId(1L);
                    return document;
                });

        DocumentResponse response =
                documentService.uploadDocument(file);

        assertEquals(1L, response.id());
        assertEquals("manual.pdf", response.filename());
        assertEquals("application/pdf", response.contentType());
        assertEquals(file.getSize(), response.size());

        try (var files = Files.list(tempDir)) {
            assertEquals(1, files.count());
        }
    }

    @Test
    void shouldRejectInvalidPdf() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "falso.pdf",
                "application/pdf",
                "This is not a real PDF"
                        .getBytes(StandardCharsets.US_ASCII)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> documentService.uploadDocument(file)
        );

        try (var files = Files.list(tempDir)) {
            assertEquals(0, files.count());
        }
    }

    @Test
    void shouldGetAllDocuments() {

        LocalDateTime uploadedAt = LocalDateTime.now();

        Document document = new Document(
                1L,
                "manual.pdf",
                "application/pdf",
                1024L,
                "uploads/manual.pdf",
                uploadedAt
        );

        when(documentRepository.findAll())
                .thenReturn(List.of(document));

        List<DocumentResponse> responses =
                documentService.getAllDocuments();

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).id());
        assertEquals("manual.pdf", responses.get(0).filename());
        assertEquals(1024L, responses.get(0).size());
        assertEquals(uploadedAt, responses.get(0).uploadedAt());

        verify(documentRepository).findAll();
    }
}