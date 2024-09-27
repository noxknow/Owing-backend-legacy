package com.ddj.owing.domain.casting.error.code;

import com.ddj.owing.global.error.code.OwingErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CastingErrorCode implements OwingErrorCode {
	CASTING_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "캐릭터를 찾을 수 없습니다."),
	CASTING_NODE_NOT_FOUND(HttpStatus.NOT_FOUND, "002", "캐릭터 노드를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	CastingErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = "CASTING" + code;
		this.message = message;
	}
}
