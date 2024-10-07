package com.ddj.owing.domain.story.model.dto.storyBlock;

public record StoryBlockPositionUpdateDto(
	Long parentBlockId,
	Integer position,
	Long storyPlotId
) {
}
