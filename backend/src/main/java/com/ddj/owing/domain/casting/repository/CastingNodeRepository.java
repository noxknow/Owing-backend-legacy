package com.ddj.owing.domain.casting.repository;

import com.ddj.owing.domain.casting.model.CastingNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("neo4jRepository")
public interface CastingNodeRepository extends Neo4jRepository<CastingNode, Long> {

    @Query("MATCH (n1:Cast{id: $fromId})-[r:CONNECTION_BY]->(n2:Cast{id: $toId}) " +
            "WHERE n1.deletedAt IS NULL AND n2.deletedAt IS NULL " +
            "SET r.name = $name")
    void updateDirectionalConnectionName(Long fromId, Long toId, String name);

    @Query("MATCH (n1:Cast{id: $fromId})-[r:CONNECTION_BY]-(n2:Cast{id: $toId}) " +
            "WHERE n1.deletedAt IS NULL AND n2.deletedAt IS NULL " +
            "SET r.name = $name")
    void updateBidirectionalConnectionName(Long fromId, Long toId, String name);

    @Query("MATCH (n:Cast{name: $name}) WHERE n.deletedAt IS NULL RETURN n")
    Optional<CastingNode> findOneByName(String name);
}
