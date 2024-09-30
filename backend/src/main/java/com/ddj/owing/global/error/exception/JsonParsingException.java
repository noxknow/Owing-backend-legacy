package com.ddj.owing.global.error.exception;

import com.ddj.owing.global.error.code.OwingErrorCode;


public class JsonParsingException extends OwingException{
	private JsonParsingException(OwingErrorCode errorCode) {
		super(errorCode);
	}

	public static JsonParsingException of(OwingErrorCode errorCode) {
        return new JsonParsingException(errorCode);
    }
}
