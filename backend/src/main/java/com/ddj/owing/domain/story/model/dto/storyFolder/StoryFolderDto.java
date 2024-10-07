package com.ddj.owing.domain.story.model.dto.storyFolder;

import com.ddj.owing.domain.story.model.StoryFolder;

import lombok.Builder;

@Builder
public record StoryFolderDto(
	Long id,
	String name,
	String description,
	Integer position
) {

	public static StoryFolderDto from(StoryFolder storyFolder) {
		return StoryFolderDto.builder()
			.id(storyFolder.getId())
			.name(storyFolder.getName())
			.description(storyFolder.getDescription())
			.position(storyFolder.getPosition())
			.build();
	}

}
