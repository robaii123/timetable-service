package com.gamacore.rag_service.service;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
public class PdfIngestionService {

    public record Chunk(String text, int page, int ord) {}

    public List<Chunk> extractChunks(MultipartFile pdf,
                                     int chunkSize,      // e.g. 900
                                     int overlap)        // e.g. 150
    {
        try (PDDocument doc = Loader.loadPDF(pdf.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            int pages = doc.getNumberOfPages();

            List<Chunk> out = new ArrayList<>();
            int ord = 0;

            for (int p = 1; p <= pages; p++) {
                stripper.setStartPage(p);
                stripper.setEndPage(p);
                String pageText = stripper.getText(doc);

                int step = Math.max(1, chunkSize - overlap);
                for (int i = 0; i < pageText.length(); i += step) {
                    int end = Math.min(pageText.length(), i + chunkSize);
                    String slice = pageText.substring(i, end).trim();
                    if (!slice.isEmpty()) {
                        out.add(new Chunk(slice, p, ord++));
                    }
                }
            }
            return out;
        } catch (Exception e) {
            throw new RuntimeException("PDF extract failed", e);
        }
    }
}
