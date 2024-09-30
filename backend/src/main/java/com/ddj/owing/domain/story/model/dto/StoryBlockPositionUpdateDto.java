package com.ddj.owing.domain.story.model.dto;

public record StoryBlockPositionUpdateDto(
	Long parentBlockId,
	Integer position,
	Long storyPlotId
) {
}
