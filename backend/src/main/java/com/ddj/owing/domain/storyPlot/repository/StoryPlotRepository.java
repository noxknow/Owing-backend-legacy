package com.ddj.owing.domain.storyPlot.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ddj.owing.domain.storyPlot.model.StoryPlot;

public interface StoryPlotRepository extends JpaRepository<StoryPlot, Long> {

	List<StoryPlot> findByStoryFolderId(Long storyFolderId);
}
