package com.portfolio.documentservice.pdf;

import java.io.IOException;
import java.nio.file.Path;

public interface PdfTextExtractor {

    String extractText(Path pdfPath) throws IOException;

}