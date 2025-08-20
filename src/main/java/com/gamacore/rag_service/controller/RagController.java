// src/main/java/com/gamacore/rag_service/controller/RagController.java
package com.gamacore.rag_service.controller;

import com.gamacore.rag_service.service.IngestionPipelineService;
import com.gamacore.rag_service.service.RagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RagController {

    private final IngestionPipelineService ingestion;
    private final RagService ragService;

    public RagController(IngestionPipelineService ingestion, RagService ragService) {
        this.ingestion = ingestion;
        this.ragService = ragService;
    }

    @PostMapping(
            path = "/ingest",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Map<String, Object> ingest(@RequestPart("file") MultipartFile file,
                                      @RequestParam(required = false) String title,
                                      @RequestParam(required = false, defaultValue = "default") String tenant)
            throws Exception {
        UUID id = ingestion.ingestPdf(file, title, tenant);
        return Map.of("documentId", id.toString(), "stored", true, "tenant", tenant);
    }

    @PostMapping(path = "/ask",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> askJson(@RequestBody Map<String, Object> body) {
        String question = (String) body.getOrDefault("question", body.get("q"));
        if (question == null || question.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "`question` is required");
        }
        String tenant = String.valueOf(body.getOrDefault("tenant", "default"));
        int k = asInt(body.get("k"), 6);

        String answer = ragService.ask(question, tenant, k);
        return Map.of("answer", answer, "topK", k, "tenant", tenant);
    }

    @GetMapping(path = "/ask", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> askGet(@RequestParam("q") String q,
                                      @RequestParam(name = "k", defaultValue = "6") int k,
                                      @RequestParam(name = "tenant", defaultValue = "default") String tenant) {
        if (q == null || q.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "`q` is required");
        }
        String answer = ragService.ask(q, tenant, k);
        return Map.of("answer", answer, "topK", k, "tenant", tenant);
    }

    @GetMapping(path = "/chat", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> chat(@RequestParam("q") String q) {
        if (q == null || q.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "`q` is required");
        }
        return Map.of("answer", ragService.chatOnly(q));
    }

    private static int asInt(Object v, int def) {
        if (v == null) return def;
        if (v instanceof Number n) return n.intValue();
        try { return Integer.parseInt(v.toString().trim()); } catch (Exception ignored) { return def; }
    }
}
