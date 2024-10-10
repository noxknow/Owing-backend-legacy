package com.ddj.owing.domain.story.model;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Content {

	private String type;
	private String text;

	@JdbcTypeCode(SqlTypes.JSON)
	private String styles;

	@Builder.Default
	private Map<String, Object> additionalFields = new HashMap<>();

	@JsonAnyGetter
	public Map<String, Object> getAdditionalFields() {
		return additionalFields;
	}

	@JsonAnySetter
	public void setAdditionalField(String key, Object value) {
		this.additionalFields.put(key, value);
	}

}
