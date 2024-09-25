package com.ddj.owing.global.error.exception;

import com.ddj.owing.global.error.code.OwingErrorCode;

public class ProjectNotFoundException extends OwingException {

    private ProjectNotFoundException(OwingErrorCode errorCode) {
        super(errorCode);
    }

    public static ProjectNotFoundException of(OwingErrorCode errorCode) {
        return new ProjectNotFoundException(errorCode);
    }
}
