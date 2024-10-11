package com.ddj.owing.domain.universe.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ddj.owing.domain.universe.model.UniverseFolder;

@Repository
public interface UniverseFolderRepository extends JpaRepository<UniverseFolder, Long> {
	List<UniverseFolder> findByProjectId(Long projectId);
}
