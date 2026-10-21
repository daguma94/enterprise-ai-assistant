package com.portfolio.documentservice.exception;

public class DocumentNotFoundException extends RuntimeException {

    public DocumentNotFoundException(Long id) {
        super("Document not found with id: " + id);
    }
}