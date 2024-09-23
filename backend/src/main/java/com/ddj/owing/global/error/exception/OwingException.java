package com.ddj.owing.global.error.exception;

import com.ddj.owing.global.error.code.OwingErrorCode;

import lombok.Getter;

@Getter
public class OwingException extends RuntimeException{
	private final OwingErrorCode errorCode;

	protected OwingException(OwingErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	protected static OwingException of(OwingErrorCode errorCode){
		return new OwingException(errorCode);
	}
}
