package com.ddj.owing.domain.storyFolder.error.code;

import org.springframework.http.HttpStatus;

import com.ddj.owing.global.error.code.OwingErrorCode;

import lombok.Getter;

@Getter
public enum StoryFolderErrorCode implements OwingErrorCode {
	FOLDER_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "폴더를 찾을 수 없습니다."),
	INVALID_POSITION(HttpStatus.BAD_REQUEST, "002", "폴더 위치가 올바르지 않습니다."),
	;

	private final HttpStatus status;
	private final String code;
	private final String message;

	StoryFolderErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = "Folder" + code;
		this.message = message;
	}
}
