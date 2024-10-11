package com.ddj.owing.domain.casting.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ddj.owing.domain.casting.model.CastingFolder;

public interface CastingFolderRepository extends JpaRepository<CastingFolder, Long> {
	List<CastingFolder> findAllByProjectIdOrderByPositionAsc(Long projectId);

	@Modifying
	@Query("update CastingFolder sf set sf.position = sf.position - 1 where sf.position > :position and sf.projectId = :projectId")
	void decrementPositionAfter(Integer position, Long projectId);

	@Query("SELECT COALESCE(MAX(position) + 1, 0) FROM CastingFolder WHERE projectId = :projectId")
	Integer findMaxOrderByProjectId(Long projectId);

	@Modifying
	@Query("update CastingFolder sf set sf.position = sf.position - 1 where sf.position between :start and :end and sf.projectId = :projectId")
	void decrementPositionBetween(Integer start, Integer end, Long projectId);

	@Modifying
	@Query("update CastingFolder sf set sf.position = sf.position + 1 where sf.position between :start and :end and sf.projectId = :projectId")
	void incrementPositionBetween(Integer start, Integer end, Long projectId);
}
