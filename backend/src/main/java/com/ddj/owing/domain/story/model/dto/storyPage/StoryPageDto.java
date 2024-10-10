package com.ddj.owing.domain.story.model.dto.storyPage;

import java.util.List;

import com.ddj.owing.domain.story.model.StoryPage;
import com.ddj.owing.domain.story.model.StoryPlot;

import lombok.Builder;

@Builder
public record StoryPageDto(
	Long storyPlotId,
	List<StoryPageBlockDto> blocks
) {

	public static StoryPageDto from(StoryPage storyPage) {
		return StoryPageDto.builder()
			.storyPlotId(storyPage.getStoryPlot().getId())
			.blocks(storyPage.getBlocks().stream().map(StoryPageBlockDto::from).toList())
			.build();
	}

	public StoryPage toEntity(StoryPlot storyPlot) {
		return StoryPage.builder()
			.storyPlot(storyPlot)
			.blocks(blocks.stream().map(StoryPageBlockDto::toEntity).toList())
			.build();
	}
}
