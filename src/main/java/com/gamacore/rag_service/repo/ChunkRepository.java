package com.gamacore.rag_service.repo;

import com.gamacore.rag_service.model.Chunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface ChunkRepository extends JpaRepository<Chunk, UUID> {

    @Query(
            value = """
            SELECT c.*
            FROM chunk c
            ORDER BY c.embedding <=> CAST(:queryVec AS vector)
            LIMIT :k
            """,
            nativeQuery = true
    )
    List<Chunk> searchTopK(@Param("queryVec") String queryVec,
                           @Param("k") int k);


    @Query(
            value = """
            SELECT c.*, (c.embedding <=> CAST(:queryVec AS vector)) AS distance
            FROM chunk c
            ORDER BY distance
            LIMIT :k
            """,
            nativeQuery = true
    )
    List<Object[]> searchTopKWithDistance(@Param("queryVec") String queryVec,
                                          @Param("k") int k);
}

