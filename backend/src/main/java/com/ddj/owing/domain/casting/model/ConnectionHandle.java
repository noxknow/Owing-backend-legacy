package com.ddj.owing.domain.casting.model;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;

public enum ConnectionHandle {
    TOP("top"), BOTTOM("bottom"), RIGHT("right"), LEFT("left");

    final String value;

    ConnectionHandle(String value) {
        this.value = value;
    }

    public static ConnectionHandle of(String value) {
        for (ConnectionHandle handle : ConnectionHandle.values()) {
            if (handle.value.equalsIgnoreCase(value)) {
                return handle;
            }
        }
        throw CastingException.of(CastingErrorCode.ILLEGAL_ARGS);
    }

}
