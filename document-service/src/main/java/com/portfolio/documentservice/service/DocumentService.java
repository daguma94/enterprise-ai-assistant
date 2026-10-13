package com.portfolio.documentservice.service;

import com.portfolio.documentservice.dto.DocumentResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface DocumentService {

    DocumentResponse uploadDocument(MultipartFile file)
            throws IOException;

    List<DocumentResponse> getAllDocuments();
}