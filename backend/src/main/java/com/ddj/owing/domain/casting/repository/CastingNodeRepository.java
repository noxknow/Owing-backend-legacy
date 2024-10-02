package com.ddj.owing.domain.casting.repository;

import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.model.CastingRelationship;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("neo4jRepository")
public interface CastingNodeRepository extends Neo4jRepository<CastingNode, Long> {

    @Query("MATCH (n1:Cast{id: $sourceId})-[r:CONNECTION{uuid: $uuid}]->(n2:Cast{id: $targetId}) " +
            "WHERE n1.deletedAt IS NULL AND n2.deletedAt IS NULL " +
            "SET r.name = $name " +
            "RETURN r.uuid AS uuid, r.name AS name, r.sourceId AS sourceId, r.targetId AS targetId, " +
            "r.sourceHandle AS sourceHandle, r.targetHandle AS targetHandle")
    Optional<CastingRelationship> updateDirectionalConnectionName(String uuid, Long sourceId, Long targetId, String name);

    @Query("MATCH (n1:Cast{id: $sourceId})-[r:BI_CONNECTION{uuid: $uuid}]-(n2:Cast{id: $targetId}) " +
            "WHERE n1.deletedAt IS NULL AND n2.deletedAt IS NULL " +
            "SET r.name = $name " +
            "RETURN r.uuid AS uuid, r.name AS name, r.sourceId AS sourceId, r.targetId AS targetId, " +
            "r.sourceHandle AS sourceHandle, r.targetHandle AS targetHandle")
    Optional<CastingRelationship> updateBidirectionalConnectionName(String uuid, Long sourceId, Long targetId, String name);

    @Query("MATCH (n:Cast{name: $name}) WHERE n.deletedAt IS NULL RETURN n")
    Optional<CastingNode> findOneByName(String name);
}
