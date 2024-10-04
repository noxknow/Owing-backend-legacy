package com.ddj.owing.domain.story.repository;

import com.ddj.owing.domain.story.model.StoryPlotNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoryPlotNodeRepository extends Neo4jRepository<StoryPlotNode, Long> {

    @Query("MATCH (n:StoryPlot{id: $id}) WHERE n.deletedAt IS NULL return n")
    Optional<StoryPlotNode> findById(Long id);
}
