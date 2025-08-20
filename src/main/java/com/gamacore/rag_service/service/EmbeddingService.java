// src/main/java/.../service/EmbeddingService.java
package com.gamacore.rag_service.service;

import org.springframework.stereotype.Service;
import org.springframework.ai.embedding.EmbeddingModel;

import java.util.List;

@Service
public class EmbeddingService {
    private final EmbeddingModel embeddingModel;

    public EmbeddingService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public float[] embedText(String text) {
        return embeddingModel
                .embed(text);
    }

}
