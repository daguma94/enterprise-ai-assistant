package com.portfolio.documentservice.service;

import com.portfolio.documentservice.dto.DocumentResponse;
import com.portfolio.documentservice.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.portfolio.documentservice.entity.Document;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    @Value("${app.storage.location}")
    private String storageLocation;

    @PostConstruct
    public void init() throws IOException {

        Path storagePath = Paths.get(storageLocation)
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(storagePath);
    }

    @Override
    public DocumentResponse uploadDocument(MultipartFile file)
            throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File cannot be empty");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("Invalid filename");
        }

        String contentType = file.getContentType();

        if (!"application/pdf".equals(contentType) || !isPdf(file)) {
        throw new IllegalArgumentException("Invalid PDF file");
        }

        String storedFilename = UUID.randomUUID() + ".pdf";

        Path storagePath = Paths.get(storageLocation)
                .toAbsolutePath()
                .normalize();

        Path destination = storagePath.resolve(storedFilename);

        Files.copy(file.getInputStream(), destination);

        Document document = new Document();
        document.setFilename(originalFilename);
        document.setContentType(contentType);
        document.setSize(file.getSize());
        document.setStoragePath(destination.toString());
        document.setUploadedAt(LocalDateTime.now());

        Document savedDocument;

        try {
            savedDocument = documentRepository.save(document);
        } catch (RuntimeException ex) {
            Files.deleteIfExists(destination);
            throw ex;
        }

        return toResponse(savedDocument);
    }

    @Override
    public List<DocumentResponse> getAllDocuments() {

        return documentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getFilename(),
                document.getContentType(),
                document.getSize(),
                document.getUploadedAt()
        );
    }

    private boolean isPdf(MultipartFile file) throws IOException {

        byte[] header;

        try (var inputStream = file.getInputStream()) {
            header = inputStream.readNBytes(5);
        }

        String signature = new String(
                header,
                StandardCharsets.US_ASCII
        );

        return "%PDF-".equals(signature);
    }
}