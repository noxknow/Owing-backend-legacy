package com.ddj.owing.domain.story.model.dto;

import jakarta.validation.constraints.NotNull;

public record StoryPlotPositionUpdateDto(
	@NotNull Integer position,
	@NotNull Long folderId
) {
}
