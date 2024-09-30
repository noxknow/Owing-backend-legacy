package com.ddj.owing.global.util;

import java.util.Map;

import com.ddj.owing.global.error.code.GlobalErrorCode;
import com.ddj.owing.global.error.exception.JsonParsingException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHelperUtil {
	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static String toJsonString(Object object) {
		try {
			return objectMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw JsonParsingException.of(GlobalErrorCode.JSON_SERIALIZATION_ERROR);
		}
	}

	public static Map<String, Object> fromJsonString(String json){
		try {
			return objectMapper.readValue(json, Map.class);
		} catch (JsonProcessingException e) {
			throw JsonParsingException.of(GlobalErrorCode.JSON_PARSING_ERROR);
		}
	}
}
