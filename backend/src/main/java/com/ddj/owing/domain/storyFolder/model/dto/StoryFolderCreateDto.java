package com.ddj.owing.domain.storyFolder.model.dto;

import com.ddj.owing.domain.storyFolder.model.StoryFolder;

public record StoryFolderCreateDto(
	Long projectId,
	String name,
	String description,
	Integer position
) {

	public StoryFolder toEntity() {
		return StoryFolder.builder()
			.projectId(projectId)
			.name(name)
			.description(description)
			.position(position)
			.build();
	}
}