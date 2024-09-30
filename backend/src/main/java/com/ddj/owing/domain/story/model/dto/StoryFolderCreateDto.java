package com.ddj.owing.domain.story.model.dto;

import com.ddj.owing.domain.story.model.StoryFolder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record StoryFolderCreateDto(
	@NotNull Long projectId,
	@NotBlank String name,
	String description
) {

	public StoryFolder toEntity(Integer position) {
		return StoryFolder.builder()
			.projectId(projectId)
			.name(name)
			.description(description)
			.position(position)
			.build();
	}
}