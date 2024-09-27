package com.ddj.owing.domain.casting.repository;

import com.ddj.owing.domain.casting.model.CastingNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("neo4jRepository")
public interface CastingNodeRepository extends Neo4jRepository<CastingNode, Long> {
    Optional<CastingNode> findOneByName(String name);
}
