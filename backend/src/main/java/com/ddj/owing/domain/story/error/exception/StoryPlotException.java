package com.ddj.owing.domain.story.error.exception;

import com.ddj.owing.domain.story.error.code.StoryPlotErrorCode;
import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class StoryPlotException extends OwingException {
	private StoryPlotException(OwingErrorCode errorCode) {
		super(errorCode);
	}

	public static StoryPlotException of(StoryPlotErrorCode errorCode) {
		return new StoryPlotException(errorCode);
	}
}
