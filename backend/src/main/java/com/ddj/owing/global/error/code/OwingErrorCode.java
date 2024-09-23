package com.ddj.owing.global.error.code;

import org.springframework.http.HttpStatus;

public interface OwingErrorCode{
	HttpStatus getStatus();
	String getCode();
	String getMessage();
}
