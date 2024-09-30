package com.ddj.owing.domain.project.error.exception;

import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class ProjectException extends OwingException {

    private ProjectException(OwingErrorCode errorCode) {
        super(errorCode);
    }

    public static ProjectException of(OwingErrorCode errorCode) {
        return new ProjectException(errorCode);
    }
}
