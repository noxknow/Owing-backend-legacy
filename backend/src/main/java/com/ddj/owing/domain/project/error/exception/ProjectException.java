package com.ddj.owing.domain.project.error.exception;

import com.ddj.owing.domain.project.error.code.ProjectErrorCode;
import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class ProjectException extends OwingException {

    private ProjectException(ProjectErrorCode errorCode) {
        super(errorCode);
    }

    public static ProjectException of(ProjectErrorCode errorCode) {
        return new ProjectException(errorCode);
    }
}
