package com.gamacore.rag_service.model;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    private UUID id;

    @Column(name = "tenant_id", nullable = false)
    private String tenantId = "default";

    private String title;
    private String uri;

    private Integer version = 1;

    @Column(name = "created_at")
    private Instant createdAt;
}

