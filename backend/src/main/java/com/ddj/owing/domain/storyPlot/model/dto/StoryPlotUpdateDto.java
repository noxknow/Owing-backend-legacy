package com.ddj.owing.domain.storyPlot.model.dto;

public record StoryPlotUpdateDto(
	String name,
	String description,
	Integer position,
	Long folderId
) {
}