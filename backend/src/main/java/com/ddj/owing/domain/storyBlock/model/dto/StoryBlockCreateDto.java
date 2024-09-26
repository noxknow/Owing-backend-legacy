package com.ddj.owing.domain.storyBlock.model.dto;

import com.ddj.owing.domain.storyBlock.model.StoryBlock;
import com.ddj.owing.domain.storyPlot.model.StoryPlot;

public record StoryBlockCreateDto(
	String type,
	String props,
	String content,
	Long parentBlockId,
	Integer position,
	Long storyPlotId
) {

	public StoryBlock toEntity(StoryPlot storyPlot, StoryBlock parentBlock) {
		return StoryBlock.builder()
			.type(type)
			.props(props)
			.content(content)
			.parentBlock(parentBlockId == null ? null : parentBlock)
			.position(position)
			.storyPlot(storyPlot)
			.build();
	}
}