package com.example.AiResumeMatcher.services.internal;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class PdfExtractor {

    public String extractTextFromPdf(MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) {
            throw new IllegalArgumentException("No file provided");
        }

        // RandomAccessRead is for random byte for fast, efficient reading [used this insted of readAllBytes]
        byte[] bytes = file.getBytes();
try (PDDocument document = Loader.loadPDF(bytes)){
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document).trim();
        }

//        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
//            throw new IllegalArgumentException("File must be a PDF");
//        }
//
//        // PDFBox 3.0+: load from byte array (easiest & most reliable for MultipartFile)
//        byte[] bytes = file.getBytes();  // loads whole file into memory
//
//        try (PDDocument document = Loader.loadPDF(bytes)) {
//            PDFTextStripper stripper = new PDFTextStripper();
//            String text = stripper.getText(document);
//            return text != null ? text.trim() : "";
//        }

    }
}
