package com.ddj.owing.domain.universe.error.exception;

import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class UniverseFileException extends OwingException {

    private UniverseFileException(OwingErrorCode errorCode) {
        super(errorCode);
    }

    public static UniverseFileException of(OwingErrorCode errorCode) {
        return new UniverseFileException(errorCode);
    }
}
