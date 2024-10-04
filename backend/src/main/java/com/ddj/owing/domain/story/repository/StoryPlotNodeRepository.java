package com.ddj.owing.domain.story.repository;

import com.ddj.owing.domain.story.model.StoryPlotNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoryPlotNodeRepository extends Neo4jRepository<StoryPlotNode, Long> {
}
