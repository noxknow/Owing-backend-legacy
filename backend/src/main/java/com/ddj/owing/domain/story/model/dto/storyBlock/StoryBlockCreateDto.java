package com.ddj.owing.domain.story.model.dto.storyBlock;

import java.util.List;
import java.util.Map;

import com.ddj.owing.domain.story.model.StoryBlock;
import com.ddj.owing.domain.story.model.StoryPlot;

public record StoryBlockCreateDto(
	String type,
	Map<String, Object> props,
	List<ContentDto> content,
	Long parentBlockId,
	Long storyPlotId
) {

	public StoryBlock toEntity(StoryPlot storyPlot, StoryBlock parentBlock, Integer position) {
		return StoryBlock.builder()
			.type(type)
			.props(props)
			.content(content == null ? null : content.stream().map(ContentDto::toEntity).toList())
			.parentBlock(parentBlockId == null ? null : parentBlock)
			.position(position)
			.storyPlot(storyPlot)
			.build();
	}
}