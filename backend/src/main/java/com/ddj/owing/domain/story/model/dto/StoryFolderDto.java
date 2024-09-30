package com.ddj.owing.domain.story.model.dto;

import com.ddj.owing.domain.story.model.StoryFolder;

import lombok.Builder;

@Builder
public record StoryFolderDto(
	String name,
	String description,
	Integer position
) {

	public static StoryFolderDto from(StoryFolder storyFolder) {
		return StoryFolderDto.builder()
			.name(storyFolder.getName())
			.description(storyFolder.getDescription())
			.position(storyFolder.getPosition())
			.build();
	}

}
