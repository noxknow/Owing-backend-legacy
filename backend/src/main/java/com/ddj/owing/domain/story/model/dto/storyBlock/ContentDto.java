package com.ddj.owing.domain.story.model.dto.storyBlock;

import java.util.Map;

import com.ddj.owing.domain.story.model.Content;
import com.ddj.owing.global.util.JsonHelperUtil;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContentDto(
	String type,
	String text,
	Map<String, Object> styles
) {

	public static ContentDto from(Content content) {
		return ContentDto.builder()
			.type(content.getType())
			.text(content.getText())
			.styles(content.getStyles() != null ? JsonHelperUtil.fromJsonString(content.getStyles()) : null)
			.build();
	}

	public Content toEntity() {
		return Content.builder()
			.type(type)
			.text(text)
			.styles(JsonHelperUtil.toJsonString(styles))
			.build();
	}

}
