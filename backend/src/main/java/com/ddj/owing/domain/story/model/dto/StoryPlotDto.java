package com.ddj.owing.domain.story.model.dto;

import com.ddj.owing.domain.story.model.StoryPlot;

import lombok.Builder;

@Builder
public record StoryPlotDto(
	String name,
	String description,
	Integer position,
	int textCount
) {

	public static StoryPlotDto from(StoryPlot storyPlot) {
		return StoryPlotDto.builder()
			.name(storyPlot.getName())
			.description(storyPlot.getDescription())
			.position(storyPlot.getPosition())
			.textCount(storyPlot.getTextCount())
			.build();
	}

}
