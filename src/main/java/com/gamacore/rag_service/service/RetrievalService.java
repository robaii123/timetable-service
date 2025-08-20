package com.gamacore.rag_service.service;

import com.gamacore.rag_service.util.VectorUtils;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetrievalService {

    private final EmbeddingModel embeddingModel;
    private final JdbcTemplate jdbc;

    public RetrievalService(EmbeddingModel embeddingModel, JdbcTemplate jdbc) {
        this.embeddingModel = embeddingModel;
        this.jdbc = jdbc;
    }

    public List<String> topK(String question, int k) {
        return topK(question, "default", k);
    }

    public List<String> topK(String question, String tenant, int k) {
        if (tenant == null || tenant.isBlank()) tenant = "default";

        float[] qvec = embeddingModel.embed(question);
        String literal = VectorUtils.toPgVectorLiteral(qvec);

        return jdbc.query(
                """
                select c.text
                from chunks c
                join documents d on d.id = c.doc_id
                where d.tenant_id = ?
                order by c.embedding <=> cast(? as vector)
                limit ?
                """,
                (rs, rowNum) -> rs.getString(1),
                tenant, literal, k
        );
    }

    public String buildContext(String question, int k) {
        return buildContext(question, "default", k);
    }

    public String buildContext(String question, String tenant, int k) {
        List<String> hits = topK(question, tenant, k);
        return String.join("\n---\n", hits);
    }
}
