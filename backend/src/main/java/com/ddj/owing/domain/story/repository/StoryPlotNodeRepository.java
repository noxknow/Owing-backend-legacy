package com.ddj.owing.domain.story.repository;

import com.ddj.owing.domain.story.model.StoryPlotNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryPlotNodeRepository extends Neo4jRepository<StoryPlotNode, Long> {

    @Query("MATCH (n1:StoryPlot{id: $id}) " +
            "WHERE n1.deletedAt IS NULL " +
            "OPTIONAL MATCH (n1)-[r]-(n2:Cast) " +
            "WHERE n2 IS NULL OR n2.deletedAt IS NULL " +
            "RETURN n1, collect(r), collect(n2)")
    Optional<StoryPlotNode> findById(Long id);

    @Query("MATCH (n1:StoryPlot{id: $storyPlotId})-[r]-(n2:Cast{id: $castId}) " +
            "DELETE r " +
            "RETURN count(DISTINCT r)")
    Integer deleteAppearedCasting(Long storyPlotId, Long castId);
}
