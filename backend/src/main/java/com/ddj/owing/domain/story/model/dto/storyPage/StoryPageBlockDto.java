package com.ddj.owing.domain.story.model.dto.storyPage;

import java.util.List;
import java.util.Map;

import com.ddj.owing.domain.story.model.StoryPage;
import com.ddj.owing.domain.story.model.dto.storyBlock.ContentDto;

import lombok.Builder;

@Builder
public record StoryPageBlockDto(
	String id,
	String type,
	Map<String, Object> props,
	List<ContentDto> content,
	List<StoryPageBlockDto> children

) {

	public static StoryPageBlockDto from(StoryPage.StoryPageBlock storyPageBlock) {
		return StoryPageBlockDto.builder()
			.id(storyPageBlock.getId())
			.type(storyPageBlock.getType())
			.props(storyPageBlock.getProps())
			.content(storyPageBlock.getContent() == null ? null :
				storyPageBlock.getContent().stream().map(ContentDto::from).toList())
			.children(storyPageBlock.getChildren().stream().map(StoryPageBlockDto::from).toList())
			.build();
	}

	public StoryPage.StoryPageBlock toEntity() {
		return StoryPage.StoryPageBlock.builder()
			.id(id)
			.type(type)
			.props(props)
			.content(content == null ? null :
				content.stream().map(ContentDto::toEntity).toList())
			.children(children.stream().map(StoryPageBlockDto::toEntity).toList())
			.build();
	}
}
