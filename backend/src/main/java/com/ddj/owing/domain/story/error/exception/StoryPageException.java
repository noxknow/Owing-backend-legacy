package com.ddj.owing.domain.story.error.exception;

import com.ddj.owing.domain.story.error.code.StoryPageErrorCode;
import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class StoryPageException extends OwingException {
	private StoryPageException(OwingErrorCode errorCode) {
		super(errorCode);
	}

	public static StoryPageException of(StoryPageErrorCode errorCode) {
		return new StoryPageException(errorCode);
	}
}
