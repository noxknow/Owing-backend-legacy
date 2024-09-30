package com.ddj.owing.domain.storyPlot.model.dto;

import jakarta.validation.constraints.NotBlank;

public record StoryPlotUpdateDto(
	@NotBlank String name,
	String description
) {
}