package com.ddj.owing.domain.story.model.dto.storyBlock;

import java.util.List;
import java.util.Map;

public record StoryBlockUpdateDto(
	String type,
	Map<String, Object> props,
	List<ContentDto> contents
) {
}