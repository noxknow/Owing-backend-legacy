package com.ddj.owing.domain.story.error.code;

import org.springframework.http.HttpStatus;

import com.ddj.owing.global.error.code.OwingErrorCode;

import lombok.Getter;

@Getter
public enum StoryBlockErrorCode implements OwingErrorCode {
	BLOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "블록을 찾을 수 없습니다."),
	INVALID_POSITION(HttpStatus.BAD_REQUEST, "002", "위치가 올바르지 않습니다."),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;

	StoryBlockErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = "Block" + code;
		this.message = message;
	}
}
