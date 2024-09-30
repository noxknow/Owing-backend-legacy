package com.ddj.owing.domain.universe.error.code;

import com.ddj.owing.global.error.code.OwingErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UniverseFileErrorCode implements OwingErrorCode {

    UNIVERSE_FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "일치하는 세계관을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    UniverseFileErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = "UNIVERSE" + code;
        this.message = message;
    }
}
