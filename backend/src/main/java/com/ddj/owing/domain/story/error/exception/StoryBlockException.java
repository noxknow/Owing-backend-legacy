package com.ddj.owing.domain.story.error.exception;

import com.ddj.owing.domain.story.error.code.StoryBlockErrorCode;
import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class StoryBlockException extends OwingException {
	private StoryBlockException(OwingErrorCode errorCode) {
		super(errorCode);
	}

	public static StoryBlockException of(StoryBlockErrorCode errorCode) {
		return new StoryBlockException(errorCode);
	}
}
