package com.ddj.owing.domain.story.model.dto;

import java.util.List;

public record StoryBlockUpdateDto(
	String type,
	String props,
	List<ContentDto> contents
) {
}