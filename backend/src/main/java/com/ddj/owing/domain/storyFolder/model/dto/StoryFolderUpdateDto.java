package com.ddj.owing.domain.storyFolder.model.dto;

public record StoryFolderUpdateDto(
	String name,
	String description,
	Integer position
) {
}