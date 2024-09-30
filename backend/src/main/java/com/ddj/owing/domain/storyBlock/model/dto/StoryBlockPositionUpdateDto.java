package com.ddj.owing.domain.storyBlock.model.dto;

public record StoryBlockPositionUpdateDto(
	Long parentBlockId,
	Integer position,
	Long storyPlotId
) {
}
