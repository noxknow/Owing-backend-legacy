package com.ddj.owing.domain.story.model.dto.storyFolder;

import jakarta.validation.constraints.NotBlank;

public record StoryFolderUpdateDto(
	@NotBlank String name,
	String description
) {
}