package com.ddj.owing.domain.story.model.dto;

import com.ddj.owing.domain.story.model.StoryFolder;
import com.ddj.owing.domain.story.model.StoryPlot;

import com.ddj.owing.domain.story.model.StoryPlotNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoryPlotCreateDto(
	@NotBlank String name,
	String description,
	@NotNull Long folderId
) {

	public StoryPlot toEntity(StoryFolder storyFolder, Integer position) {
		return StoryPlot.builder()
			.name(name)
			.description(description)
			.position(position)
			.storyFolder(storyFolder)
			.build();
	}

	public StoryPlotNode toNode(Long storyPlotId) {
		return new StoryPlotNode(storyPlotId, name);
	}
}