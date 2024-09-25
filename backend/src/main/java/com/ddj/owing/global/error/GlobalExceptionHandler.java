package com.ddj.owing.global.error;

import java.util.Arrays;

import com.ddj.owing.global.error.exception.ProjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.ddj.owing.global.error.code.GlobalErrorCode;
import com.ddj.owing.global.error.dto.ErrorResponse;
import com.ddj.owing.global.error.exception.AuthException;
import com.ddj.owing.global.error.exception.OwingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorResponse> handleOwingException (OwingException e) {
		log.error("class: {}, message: {}", e.getClass(), e.getMessage());
		log.error(Arrays.toString(e.getStackTrace()));
		return createErrorResponse(e.getErrorCode());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException (Exception e) {
		log.error("class: {}, message: {}", e.getClass(), e.getMessage());
		log.error(Arrays.toString(e.getStackTrace()));
		e.printStackTrace();
		return createErrorResponse(GlobalErrorCode.ERROR);
	}

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ErrorResponse> handleRuntimeException (RuntimeException e) {
		log.error("class: {}, message: {}", e.getClass(), e.getMessage());
		log.error(Arrays.toString(e.getStackTrace()));
		e.printStackTrace();
		return createErrorResponse(GlobalErrorCode.RUNTIME_ERROR);
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleMissingPathVariableException (NoResourceFoundException e) {
		log.error("class: {}, message: {}", e.getClass(), e.getMessage());
		log.error(Arrays.toString(e.getStackTrace()));
		e.printStackTrace();
		return createErrorResponse(GlobalErrorCode.ILLEGAL_PATH);
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleMissingPathVariableException (MethodArgumentTypeMismatchException e) {
		log.error("class: {}, message: {}", e.getClass(), e.getMessage());
		log.error(Arrays.toString(e.getStackTrace()));
		return createErrorResponse(GlobalErrorCode.ILLEGAL_PATH_ARGS);
	}

	@ExceptionHandler(AuthException.class)
	public ResponseEntity<ErrorResponse> handleAuthException (AuthException e) {
		log.error("class: {}, message: {}", e.getClass(), e.getMessage());
		log.error(Arrays.toString(e.getStackTrace()));
		return createErrorResponse(e.getErrorCode());
	}

	@ExceptionHandler(ProjectNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleProjectNotFoundException(ProjectNotFoundException e) {
		log.error("class: {}, message: {}", e.getClass(), e.getMessage());
		log.error(Arrays.toString(e.getStackTrace()));
		return createErrorResponse(e.getErrorCode());
	}
}
