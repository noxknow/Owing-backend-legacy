package com.ddj.owing.global.error.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OpenAiErrorCode implements OwingErrorCode{
	CASTING_PARSE_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "001", "인물을 추출했으나, 변환에 실패했습니다."),
	CASTING_EXTRACT_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "002", "인물 추출에 실패했습니다."),
	IMAGE_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "003", "이미지 생성에 실패했습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	OpenAiErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = "OPENAI"+code;
		this.message = message;
	}
}
