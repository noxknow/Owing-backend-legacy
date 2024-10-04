package com.ddj.owing.domain.story.error.code;

import org.springframework.http.HttpStatus;

import com.ddj.owing.global.error.code.OwingErrorCode;

import lombok.Getter;

@Getter
public enum StoryPlotErrorCode implements OwingErrorCode {
	PLOT_NOT_FOUND(HttpStatus.NOT_FOUND, "001", "플롯을 찾을 수 없습니다."),
	INVALID_POSITION(HttpStatus.BAD_REQUEST, "002", "플롯의 위치를 변경할 수 없습니다."),
    PLOT_NODE_NOT_FOUND(HttpStatus.NOT_FOUND, "003", "플롯 노드를 찾을 수 없습니다.");

	private final HttpStatus status;
	private final String code;
	private final String message;

	StoryPlotErrorCode(HttpStatus status, String code, String message) {
		this.status = status;
		this.code = "Plot" + code;
		this.message = message;
	}
}
