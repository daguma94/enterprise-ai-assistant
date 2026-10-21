package com.portfolio.documentservice.controller;

import com.portfolio.documentservice.dto.DocumentContentResponse;
import com.portfolio.documentservice.dto.DocumentResponse;
import com.portfolio.documentservice.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.CREATED)
    public DocumentResponse uploadDocument(
            @RequestParam("file") MultipartFile file)
            throws IOException {

        return documentService.uploadDocument(file);
    }

    @GetMapping
    public List<DocumentResponse> getAllDocuments() {
        return documentService.getAllDocuments();
    }

    @GetMapping("/{id}/content")
    public DocumentContentResponse getDocumentContent(
            @PathVariable Long id) {

        return documentService.getDocumentContent(id);
    }
}