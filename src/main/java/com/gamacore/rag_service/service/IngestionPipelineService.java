package com.gamacore.rag_service.service;

import com.gamacore.rag_service.util.VectorUtils;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class IngestionPipelineService {

    private final PdfIngestionService pdfIngestion;
    private final EmbeddingModel embeddingModel;
    private final JdbcTemplate jdbc;

    public IngestionPipelineService(PdfIngestionService pdfIngestion,
                                    EmbeddingModel embeddingModel,
                                    JdbcTemplate jdbc) {
        this.pdfIngestion = pdfIngestion;
        this.embeddingModel = embeddingModel;
        this.jdbc = jdbc;
    }

    @Transactional
    public UUID ingestPdf(MultipartFile file, String title, String tenant) {
        String tenantId = (tenant == null || tenant.isBlank()) ? "default" : tenant;
        String docTitle = (title == null || title.isBlank()) ? file.getOriginalFilename() : title;

        List<PdfIngestionService.Chunk> chunks = pdfIngestion.extractChunks(file, 900, 150);

        UUID docId = UUID.randomUUID();
        jdbc.update(
                "insert into documents(id, tenant_id, title, uri, version, created_at) " +
                        "values (?, ?, ?, ?, ?, ?)",
                docId,
                tenantId,
                docTitle,
                "upload:" + docTitle,
                Integer.valueOf(1),
                Timestamp.from(Instant.now())
        );

        for (PdfIngestionService.Chunk c : chunks) {
            if (c.text() == null || c.text().isBlank()) continue;

            float[] vec = embeddingModel.embed(c.text());
            String literal = VectorUtils.toPgVectorLiteral(vec);

            jdbc.update(
                    "insert into chunks(id, doc_id, page, ord, text, meta, embedding) " +
                            "values (?, ?, ?, ?, ?, '{}'::jsonb, ?::vector)",
                    UUID.randomUUID(),
                    docId,
                    c.page(),
                    c.ord(),
                    c.text(),
                    literal
            );
        }

        return docId;
    }
}


