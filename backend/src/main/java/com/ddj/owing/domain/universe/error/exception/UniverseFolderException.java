package com.ddj.owing.domain.universe.error.exception;

import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class UniverseFolderException extends OwingException {

    private UniverseFolderException(OwingErrorCode errorCode) {
        super(errorCode);
    }

    public static UniverseFolderException of(OwingErrorCode errorCode) {
        return new UniverseFolderException(errorCode);
    }
}
