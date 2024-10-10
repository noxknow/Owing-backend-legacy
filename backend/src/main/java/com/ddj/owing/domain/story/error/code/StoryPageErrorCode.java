package com.ddj.owing.domain.story.error.code;

import org.springframework.http.HttpStatus;

import com.ddj.owing.global.error.code.OwingErrorCode;

import lombok.Getter;

@Getter
public enum StoryPageErrorCode implements OwingErrorCode {
	PAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "원고를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	StoryPageErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = "Page" + code;
		this.message = message;
	}
}
