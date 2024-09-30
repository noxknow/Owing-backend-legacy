package com.ddj.owing.domain.story.error.exception;

import com.ddj.owing.domain.story.error.code.StoryFolderErrorCode;
import com.ddj.owing.global.error.code.OwingErrorCode;
import com.ddj.owing.global.error.exception.OwingException;

public class StoryFolderException extends OwingException {
	private StoryFolderException(OwingErrorCode errorCode) {
		super(errorCode);
	}

	public static StoryFolderException of(StoryFolderErrorCode errorCode) {
		return new StoryFolderException(errorCode);
	}
}
