package com.ddj.owing.domain.storyBlock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ddj.owing.domain.storyBlock.model.StoryBlock;

public interface StoryBlockRepository extends JpaRepository<StoryBlock, Long> {

	@Query("SELECT sb FROM StoryBlock sb WHERE sb.storyPlot.id = :plotId AND sb.parentBlock IS NULL ORDER BY sb.position")
	List<StoryBlock> findTopLevelBlocksByPlotId(Long plotId);

	@Query("SELECT COALESCE(MAX(sb.position), 0) FROM StoryBlock sb WHERE sb.storyPlot.id = :plotId")
	Integer findMaxOrderByStoryPlotId(Long plotId);

	@Modifying
	@Query("update StoryBlock set position = position - 1 where position between :start and :end and parentBlock.id = :parentId")
	void decrementPositionBetween(Integer start, Integer end, Long parentId);

	@Modifying
	@Query("update StoryBlock set position = position + 1 where position between :start and :end and parentBlock.id = :parentId")
	void incrementPositionBetween(Integer start, Integer end, Long parentId);

	@Modifying
	@Query("update StoryBlock set position = position - 1 where position > :position and parentBlock.id = :parentId")
	void decrementPositionAfter(Integer position, Long parentId);

	@Modifying
	@Query("update StoryBlock set position = position + 1 where position >= :position and parentBlock.id = :parentId")
	void incrementPositionAfter(Integer position, Long parentId);
}
