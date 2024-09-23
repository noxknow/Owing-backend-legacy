package com.ddj.owing.global.error.exception;

import com.ddj.owing.global.error.code.AuthErrorCode;
import com.ddj.owing.global.error.code.OwingErrorCode;

public class AuthException extends OwingException{
	private AuthException(OwingErrorCode errorCode) {
		super(errorCode);
	}

	public static AuthException of (AuthErrorCode errorCode) {
		return new AuthException(errorCode);
	}
}
