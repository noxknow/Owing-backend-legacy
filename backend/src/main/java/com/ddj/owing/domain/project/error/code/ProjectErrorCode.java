package com.ddj.owing.domain.project.error.code;

import com.ddj.owing.global.error.code.OwingErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProjectErrorCode implements OwingErrorCode {

    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "프로젝트를 찾을 수 없습니다."),
    INVALID_GENRE_COUNT(HttpStatus.BAD_REQUEST, "002", "장르의 개수가 유효하지 않습니다."),
    PROJECT_NODE_NOT_FOUND(HttpStatus.NOT_FOUND, "003", "프로젝트 노드를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ProjectErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = "PROJECT" + code;
        this.message = message;
    }
}