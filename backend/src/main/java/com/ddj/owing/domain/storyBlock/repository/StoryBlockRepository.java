package com.ddj.owing.domain.storyBlock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ddj.owing.domain.storyBlock.model.StoryBlock;

public interface StoryBlockRepository extends JpaRepository<StoryBlock, Long> {

	@Query("SELECT sb FROM StoryBlock sb WHERE sb.storyPlot.id = :plotId AND sb.parentBlock IS NULL ORDER BY sb.position")
	List<StoryBlock> findTopLevelBlocksByPlotId(Long plotId);
}
