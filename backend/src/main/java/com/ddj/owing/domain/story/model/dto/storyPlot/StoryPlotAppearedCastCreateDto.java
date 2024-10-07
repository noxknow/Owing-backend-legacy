package com.ddj.owing.domain.story.model.dto.storyPlot;

import java.util.List;

public record StoryPlotAppearedCastCreateDto(
	List<Long> castIdList
) {
}
