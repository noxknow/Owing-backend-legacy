package com.ddj.owing.domain.story.model.dto;

public record StoryBlockUpdateDto(
	String type,
	String props,
	String content
) {
}