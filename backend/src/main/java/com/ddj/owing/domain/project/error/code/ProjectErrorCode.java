package com.ddj.owing.domain.project.error.code;

import com.ddj.owing.global.error.code.OwingErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ProjectErrorCode implements OwingErrorCode {

    PROJECT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "프로젝트를 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    ProjectErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = "PROJECT" + code;
        this.message = message;
    }
}