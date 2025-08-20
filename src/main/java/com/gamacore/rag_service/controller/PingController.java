// src/main/java/com/gamacore/rag_service/controller/PingController.java
package com.gamacore.rag_service.controller;

import com.gamacore.rag_service.service.RagService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PingController {

    private final RagService rag;

    public PingController(RagService rag) {
        this.rag = rag;
    }

    @GetMapping("/ping")
    public String ping(@RequestParam(defaultValue = "hi") String q,
                       @RequestParam(defaultValue = "default") String tenant,
                       @RequestParam(defaultValue = "3") int k) {
        return rag.ask(q, tenant, k);
    }
}

