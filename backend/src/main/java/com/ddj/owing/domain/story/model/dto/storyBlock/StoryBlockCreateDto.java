package com.ddj.owing.domain.story.model.dto.storyBlock;

import java.util.List;

import com.ddj.owing.domain.story.model.StoryBlock;
import com.ddj.owing.domain.story.model.StoryPlot;

public record StoryBlockCreateDto(
	String type,
	String props,
	List<ContentDto> contents,
	Long parentBlockId,
	Long storyPlotId
) {

	public StoryBlock toEntity(StoryPlot storyPlot, StoryBlock parentBlock, Integer position) {
		return StoryBlock.builder()
			.type(type)
			.props(props)
			.contents(contents.stream().map(ContentDto::toEntity).toList())
			.parentBlock(parentBlockId == null ? null : parentBlock)
			.position(position)
			.storyPlot(storyPlot)
			.build();
	}
}