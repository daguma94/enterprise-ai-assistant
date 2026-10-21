package com.portfolio.documentservice.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfTextExtractorImplTest {

    @TempDir
    Path tempDir;

    private final PdfTextExtractor pdfTextExtractor =
            new PdfTextExtractorImpl();

    @Test
    void shouldExtractTextFromPdf() throws Exception {

        Path pdfPath = tempDir.resolve("test.pdf");

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream content =
                         new PDPageContentStream(document, page)) {

                content.beginText();
                content.setFont(
                        new PDType1Font(Standard14Fonts.FontName.HELVETICA),
                        12
                );
                content.newLineAtOffset(50, 700);
                content.showText("Enterprise AI Assistant");
                content.endText();
            }

            document.save(pdfPath.toFile());
        }

        String extractedText =
                pdfTextExtractor.extractText(pdfPath);

        assertTrue(
                extractedText.contains("Enterprise AI Assistant")
        );
    }
}