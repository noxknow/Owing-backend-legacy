package com.ddj.owing.global.error;

import org.springframework.http.ResponseEntity;

import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.dto.ErrorResponse;

public abstract class BaseExceptionHandler {
	protected ResponseEntity<ErrorResponse> createErrorResponse(OwingErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode));
	}
}
