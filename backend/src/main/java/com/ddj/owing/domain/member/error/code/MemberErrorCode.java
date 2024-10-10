package com.ddj.owing.domain.member.error.code;

import com.ddj.owing.global.error.code.OwingErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum MemberErrorCode implements OwingErrorCode {
	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "회원을 찾을 수 없습니다.");



	private final HttpStatus status;
	private final String code;
	private final String message;

	MemberErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = "MEMBER" + code;
		this.message = message;
	}
}
