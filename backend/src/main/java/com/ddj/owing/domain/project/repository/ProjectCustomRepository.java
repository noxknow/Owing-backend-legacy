package com.ddj.owing.domain.project.repository;

import com.ddj.owing.domain.project.model.Project;

import java.util.List;

public interface ProjectCustomRepository {

    List<Project> findTop3ByOrderByUpdatedAtDesc();
    List<Project> findAllByOrderByUpdatedAtDesc();
}
