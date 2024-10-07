package com.ddj.owing.domain.casting.error.exception;

import com.ddj.owing.domain.casting.error.code.CastingFolderErrorCode;
import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class CastingFolderException extends OwingException {
	private CastingFolderException(OwingErrorCode errorCode) {
		super(errorCode);
	}

	public static CastingFolderException of(CastingFolderErrorCode errorCode) {
		return new CastingFolderException(errorCode);
	}
}
