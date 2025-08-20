package com.gamacore.rag_service.repo;

import com.gamacore.rag_service.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {}
