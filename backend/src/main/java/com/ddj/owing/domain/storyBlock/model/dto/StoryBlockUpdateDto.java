package com.ddj.owing.domain.storyBlock.model.dto;

public record StoryBlockUpdateDto(
	String type,
	String props,
	String content
) {
}