package com.ddj.owing.domain.member.error.exception;

import com.ddj.owing.domain.member.error.code.MemberErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class MemberException extends OwingException {
	private MemberException(MemberErrorCode errorCode) {
		super(errorCode);
	}

	public static MemberException of(MemberErrorCode errorCode) {
		return new MemberException(errorCode);
	}
}
