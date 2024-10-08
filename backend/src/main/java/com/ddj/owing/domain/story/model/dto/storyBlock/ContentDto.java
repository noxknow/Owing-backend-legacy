package com.ddj.owing.domain.story.model.dto.storyBlock;

import java.util.HashMap;
import java.util.Map;

import com.ddj.owing.domain.story.model.Content;
import com.ddj.owing.global.util.JsonHelperUtil;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ContentDto(
	String type,
	String text,
	Map<String, Object> styles,
	Map<String, Object> additionalFields
) {

	@Builder
	public ContentDto {
		additionalFields = additionalFields != null ? additionalFields : new HashMap<>();
	}

	@JsonAnyGetter
	public Map<String, Object> additionalFields() {
		return additionalFields;
	}

	@JsonAnySetter
	public void setAdditionalField(String key, Object value) {
		additionalFields.put(key, value);
	}

	public static ContentDto from(Content content) {
		return ContentDto.builder()
			.type(content.getType())
			.text(content.getText())
			.styles(content.getStyles() != null ? JsonHelperUtil.fromJsonString(content.getStyles()) : null)
			.additionalFields(content.getAdditionalFields())
			.build();
	}

	public Content toEntity() {
		return Content.builder()
			.type(type)
			.text(text)
			.styles(JsonHelperUtil.toJsonString(styles))
			.additionalFields(additionalFields)
			.build();
	}

}
