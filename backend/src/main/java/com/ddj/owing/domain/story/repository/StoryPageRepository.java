package com.ddj.owing.domain.story.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ddj.owing.domain.story.model.StoryPage;

import io.lettuce.core.dynamic.annotation.Param;

public interface StoryPageRepository extends JpaRepository<StoryPage, Long> {
	Optional<StoryPage> findByStoryPlotId(Long storyPlotId);

	@Query(value = """
		SELECT string_agg(c ->> 'text', E'\\n') AS tx FROM story_page sp, jsonb_array_elements(sp.page_blocks) AS pb, jsonb_array_elements(pb -> 'content') AS c WHERE sp.story_plot_id = :storyPlotId""", nativeQuery = true)
	String findAllTextListByStoryPlotId(@Param("storyPlotId") Long storyPlotId);
}
