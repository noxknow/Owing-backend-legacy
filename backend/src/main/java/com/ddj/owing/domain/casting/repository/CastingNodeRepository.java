package com.ddj.owing.domain.casting.repository;

import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.model.CastingRelationship;
import com.ddj.owing.domain.casting.model.dto.CastingRelationshipInfoDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingSummaryDto;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CastingNodeRepository extends Neo4jRepository<CastingNode, Long> {

    @Query("MATCH (n1:Cast{id: $id}) " +
            "WHERE n1.deletedAt IS NULL " +
            "OPTIONAL MATCH (n1)-[r]-(n2) " +
            "WHERE n2 IS NULL OR n2.deletedAt IS NULL " +
            "RETURN n1, collect(r), collect(n2)")
    Optional<CastingNode> findById(Long id);

    @Query("MATCH (n1:Cast{id: $sourceId})-[r:CONNECTION{uuid: $uuid}]->(n2:Cast{id: $targetId}) " +
            "WHERE n1.deletedAt IS NULL AND n2.deletedAt IS NULL " +
            "SET r.label = $label " +
            "RETURN r.uuid AS uuid, r.label AS label, r.sourceId AS sourceId, r.targetId AS targetId, " +
            "r.sourceHandle AS sourceHandle, r.targetHandle AS targetHandle")
    Optional<CastingRelationship> updateDirectionalConnectionName(String uuid, Long sourceId, Long targetId, String label);

    @Query("MATCH (n1:Cast{id: $sourceId})-[r:BI_CONNECTION{uuid: $uuid}]-(n2:Cast{id: $targetId}) " +
            "WHERE n1.deletedAt IS NULL AND n2.deletedAt IS NULL " +
            "SET r.label = $label " +
            "RETURN r.uuid AS uuid, r.label AS label, r.sourceId AS sourceId, r.targetId AS targetId, " +
            "r.sourceHandle AS sourceHandle, r.targetHandle AS targetHandle")
    Optional<CastingRelationship> updateBidirectionalConnectionName(String uuid, Long sourceId, Long targetId, String label);

    @Query("MATCH (n1:Cast)-[r:CONNECTION|BI_CONNECTION{uuid: 'string'}]-(n2:Cast) " +
            "DELETE r " +
            "RETURN count(DISTINCT r)")
    Integer deleteConnectionByUuid(String uuid);

    @Query("MATCH (n1:Project{id: $projectId})-[r1:INCLUDED]->(n2:Cast) " +
            "WHERE n1.deletedAt IS NULL " +
                "AND n2.deletedAt IS NULL " +
            "RETURN n2")
    List<CastingNode> findAllByProjectId(Long projectId);

    @Query("MATCH (n1:Project{id: $projectId})-[r1:INCLUDED]->(n2:Cast)-[r2]-(n3:Cast) " +
            "WHERE n1.deletedAt IS NULL " +
                "AND n2.deletedAt IS NULL " +
                "AND n3.deletedAt IS NULL " +
            "RETURN DISTINCT " +
                "type(r2) as type, r2.uuid AS uuid, r2.label AS label, r2.sourceId AS sourceId, " +
                "r2.targetId AS targetId, r2.sourceHandle AS sourceHandle, r2.targetHandle AS targetHandle")
    List<CastingRelationshipInfoDto> findAllConnectionByProjectId(Long projectId);

    @Query("MATCH (n1:Project{id: $projectId})-[r1:INCLUDED]->(n2:Cast) " +
            "WHERE n1.deletedAt IS NULL " +
            "AND n2.deletedAt IS NULL " +
            "RETURN n2.id AS id, n2.name AS name, n2.gender AS gender")
    List<CastingSummaryDto> findAllSummaryByProjectId(Long StoryPlotId);
}
