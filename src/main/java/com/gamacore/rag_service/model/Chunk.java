package com.gamacore.rag_service.model;

import jakarta.persistence.*;
import java.util.UUID;

// Chunk entity (important bits)
@Entity
@Table(name = "chunks")
public class Chunk {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doc_id", nullable = false)
    private Document doc;

    private Integer page;
    private Integer ord;

    @Column(columnDefinition = "text", nullable = false)
    private String text;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String meta = "{}";

}


