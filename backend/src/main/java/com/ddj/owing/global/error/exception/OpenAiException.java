package com.ddj.owing.global.error.exception;

import com.ddj.owing.global.error.code.OpenAiErrorCode;


public class OpenAiException extends OwingException{
	private OpenAiException(OpenAiErrorCode errorCode) {
		super(errorCode);
	}

	public static OpenAiException of(OpenAiErrorCode errorCode) {
        return new OpenAiException(errorCode);
    }
}
