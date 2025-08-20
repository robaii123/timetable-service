// src/main/java/.../controller/DocController.java
package com.gamacore.rag_service.controller;

import com.gamacore.rag_service.service.IngestionPipelineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/docs")
public class DocController {

    private final IngestionPipelineService pipeline;

    public DocController(IngestionPipelineService pipeline) {
        this.pipeline = pipeline;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file,
                                    @RequestParam(value = "tenant", defaultValue = "default") String tenant) {
        UUID id = pipeline.ingestPdf(file, file.getOriginalFilename(), tenant);
        return ResponseEntity.ok(Map.of("count", 1, "docIds", List.of(id)));
    }

    @PostMapping("/uploadBatch")
    public ResponseEntity<?> uploadBatch(@RequestParam("files") List<MultipartFile> files,
                                         @RequestParam(value = "tenant", defaultValue = "default") String tenant) {
        List<UUID> ids = new ArrayList<>();
        for (MultipartFile f : files) {
            if (f != null && !f.isEmpty() && f.getOriginalFilename() != null
                    && f.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
                ids.add(pipeline.ingestPdf(f, f.getOriginalFilename(), tenant));
            }
        }
        return ResponseEntity.ok(Map.of("count", ids.size(), "docIds", ids));
    }
}
