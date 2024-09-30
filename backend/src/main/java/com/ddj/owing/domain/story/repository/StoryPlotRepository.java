package com.ddj.owing.domain.story.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ddj.owing.domain.story.model.StoryPlot;

public interface StoryPlotRepository extends JpaRepository<StoryPlot, Long> {

	List<StoryPlot> findByStoryFolderIdOrderByPositionAsc(Long storyFolderId);

	@Query("SELECT COALESCE(MAX(position), 0) FROM StoryPlot WHERE storyFolder.id = :folderId")
	Integer findMaxOrderByStoryFolderId(Long folderId);

	@Modifying
	@Query("update StoryPlot set position = position - 1 where position between :start and :end and storyFolder.id = :folderId")
	void decrementPositionBetween(Integer start, Integer end, Long folderId);

	@Modifying
	@Query("update StoryPlot set position = position + 1 where position between :start and :end and storyFolder.id = :folderId")
	void incrementPositionBetween(Integer start, Integer end, Long folderId);

	@Modifying
	@Query("update StoryPlot set position = position - 1 where position > :position and storyFolder.id = :folderId")
	void decrementPositionAfter(Integer position, Long folderId);

	@Modifying
	@Query("update StoryPlot set position = position + 1 where position >= :position and storyFolder.id = :folderId")
	void incrementPositionAfter(Integer position, Long folderId);
}
