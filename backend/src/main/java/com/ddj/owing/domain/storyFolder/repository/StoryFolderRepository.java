package com.ddj.owing.domain.storyFolder.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ddj.owing.domain.storyFolder.model.StoryFolder;

public interface StoryFolderRepository extends JpaRepository<StoryFolder, Long> {
	List<StoryFolder> findAllByProjectIdOrderByPositionAsc(Long projectId);

	@Modifying
	@Query("update StoryFolder sf set sf.position = sf.position - 1 where sf.position > :position and sf.projectId = :projectId")
	void decrementPositionAfter(Integer position, Long projectId);

	@Query("SELECT COALESCE(MAX(position), 0) FROM StoryFolder WHERE projectId = :projectId")
	Integer findMaxOrderByProjectId(Long projectId);

	@Modifying
	@Query("update StoryFolder sf set sf.position = sf.position - 1 where sf.position between :start and :end and sf.projectId = :projectId")
	void decrementPositionBetween(Integer start, Integer end, Long projectId);

	@Modifying
	@Query("update StoryFolder sf set sf.position = sf.position + 1 where sf.position between :start and :end and sf.projectId = :projectId")
	void incrementPositionBetween(Integer start, Integer end, Long projectId);
}
