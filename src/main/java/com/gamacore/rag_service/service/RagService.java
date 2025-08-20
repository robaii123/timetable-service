package com.gamacore.rag_service.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class RagService {
    private final ChatClient chatClient;
    private final RetrievalService retrieval;
    private final int defaultTopK = 6;

    public RagService(ChatClient chatClient, RetrievalService retrieval) {
        this.chatClient = chatClient;
        this.retrieval = retrieval;
    }

    public String ask(String question) {
        return ask(question, "default", defaultTopK);
    }

    public String ask(String question, String tenant, int topK) {
        List<String> hits = retrieval.topK(question, tenant, topK);

        if (hits == null || hits.isEmpty()) {
            return generalAnswer(question);
        }

        String ctx = String.join("\n---\n", hits.stream()
                .map(s -> s == null ? "" : s.trim())
                .map(s -> s.length() > 1200 ? s.substring(0, 1200) + " …" : s)
                .toList());

        final String SYSTEM_RAG = """
            You are a retrieval QA assistant.
            Use ONLY the provided CONTEXT to answer.
            Be concise and factual. Quote exact values (IDs, dates, amounts) if requested.
            Answer in the same language as the user's question.
            """;

        String userPrompt = """
            CONTEXT:
            <<<
            %s
            >>>

            QUESTION:
            %s

            Please answer strictly based on the context above.
            """.formatted(ctx, question);

        return chatClient
                .prompt()
                .system(SYSTEM_RAG)
                .user(userPrompt)
                .call()
                .content();
    }

    private String generalAnswer(String question) {
        final String SYSTEM_GENERAL = """
            You are a helpful and concise assistant.
            Answer in the same language as the user's question.
            """;
        return chatClient
                .prompt()
                .system(SYSTEM_GENERAL)
                .user(question)
                .call()
                .content();
    }

    public String chatOnly(String question) {
        return generalAnswer(question);
    }

    @SuppressWarnings("unused")
    private String sameLangNoInfo(String question) {
        String q = question == null ? "" : question.toLowerCase(Locale.ROOT);
        if (q.matches(".*\\b(quoi|quel|quelle|quels|quelles|comment|pourquoi|où|quand|numéro|n°|preuve|pièce)\\b.*")
                || q.contains("français") || q.contains("francais")) {
            return "Je n’ai pas trouvé d’informations pertinentes dans vos documents, je vais répondre de manière générale :";
        }
        return "I didn’t find relevant information in your documents, answering generally:";
    }
}
