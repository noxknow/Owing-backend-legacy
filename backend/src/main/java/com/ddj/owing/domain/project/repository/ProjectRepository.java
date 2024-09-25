package com.ddj.owing.domain.project.repository;

import com.ddj.owing.domain.project.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findTop3ByOrderByUpdatedAtDesc();
}
