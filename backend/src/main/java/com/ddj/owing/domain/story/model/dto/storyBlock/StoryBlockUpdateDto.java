package com.ddj.owing.domain.story.model.dto.storyBlock;

import java.util.List;

public record StoryBlockUpdateDto(
	String type,
	String props,
	List<ContentDto> contents
) {
}