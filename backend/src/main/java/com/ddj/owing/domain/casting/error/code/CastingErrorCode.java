package com.ddj.owing.domain.casting.error.code;

import com.ddj.owing.global.error.code.OwingErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CastingErrorCode implements OwingErrorCode {
	CASTING_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "캐릭터를 찾을 수 없습니다."),
	CASTING_NODE_NOT_FOUND(HttpStatus.NOT_FOUND, "002", "캐릭터 노드를 찾을 수 없습니다."),
	INVALID_ARGS_FOR_UPDATE(HttpStatus.BAD_REQUEST, "003", "업데이트 요청에 잘못된 데이터가 포함되었습니다."),
	CONNECTION_NOT_FOUND(HttpStatus.NOT_FOUND, "004", "관계를 찾을 수 없습니다."),
	ILLEGAL_ARGS(HttpStatus.BAD_REQUEST, "005", "올바르지 않은 인자값입니다."),
	CONNECTION_NAME_UPDATE_FAIL(HttpStatus.BAD_REQUEST, "006", "관계 이름 업데이트를 실패했습니다."),
	INVALID_DELETE_COUNT(HttpStatus.CONFLICT, "007", "올바르지 않은 관계 정보로 인해 삭제에 실패했습니다."),
	ILLEGAL_HANDLE_ARGS(HttpStatus.BAD_REQUEST, "008", "올바르지 않은 관계 Handle입니다."),
	ILLEGAL_TYPE_ARGS(HttpStatus.BAD_REQUEST, "009", "올바르지 않은 관계 Type입니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	CastingErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = "CASTING" + code;
		this.message = message;
	}
}
