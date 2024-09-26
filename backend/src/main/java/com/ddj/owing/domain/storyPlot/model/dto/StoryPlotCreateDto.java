package com.ddj.owing.domain.storyPlot.model.dto;

import com.ddj.owing.domain.storyFolder.model.StoryFolder;
import com.ddj.owing.domain.storyPlot.model.StoryPlot;

public record StoryPlotCreateDto(
	String name,
	String description,
	Integer position,
	Long folderId
) {

	public StoryPlot toEntity(StoryFolder storyFolder) {
		return StoryPlot.builder()
			.name(name)
			.description(description)
			.position(position)
			.storyFolder(storyFolder)
			.build();
	}
}