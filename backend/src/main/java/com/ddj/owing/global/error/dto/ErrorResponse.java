package com.ddj.owing.global.error.dto;

import com.ddj.owing.global.error.code.OwingErrorCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
	private final String code;
	private final String message;

	public static ErrorResponse of(OwingErrorCode errorCode) {
		return new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
	}
}
