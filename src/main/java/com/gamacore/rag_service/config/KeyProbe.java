// src/main/java/com/gamacore/rag_service/config/KeyProbe.java
package com.gamacore.rag_service.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KeyProbe {
    private static final Logger log = LoggerFactory.getLogger(KeyProbe.class);

    public KeyProbe(
            @Value("${spring.ai.openai.api-key:}") String openrouterKey,
            @Value("${spring.ai.openai.embedding.api-key:}") String openaiKey) {

        if (openrouterKey == null || openrouterKey.isBlank()) {
            log.warn("OPENROUTER_API_KEY is missing (spring.ai.openai.api-key). Chat will fail until it’s set.");
        } else {
            log.info("OpenRouter key: present");
        }

        if (openaiKey == null || openaiKey.isBlank()) {
            log.warn("OPENAI_API_KEY is missing (spring.ai.openai.embedding.api-key). Embeddings will fail until it’s set.");
        } else {
            log.info("OpenAI embedding key: present");
        }
    }
}
