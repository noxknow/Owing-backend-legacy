package com.ddj.owing.domain.project.repository;

import com.ddj.owing.domain.project.model.ProjectNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectNodeRepository extends Neo4jRepository<ProjectNode, Long>, ProjectCustomRepository {
}
