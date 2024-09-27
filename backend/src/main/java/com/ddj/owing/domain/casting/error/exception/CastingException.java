package com.ddj.owing.domain.casting.error.exception;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class CastingException extends OwingException {
	private CastingException(CastingErrorCode errorCode) {
		super(errorCode);
	}

	public static CastingException of(CastingErrorCode errorCode) {
		return new CastingException(errorCode);
	}
}
