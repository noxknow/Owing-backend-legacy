package com.ddj.owing.domain.casting.model;

import com.ddj.owing.domain.casting.error.code.CastingErrorCode;
import com.ddj.owing.domain.casting.error.exception.CastingException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum ConnectionHandle {
    TOP("top"), BOTTOM("bottom"), RIGHT("right"), LEFT("left");

    final String value;

    ConnectionHandle(String value) {
        this.value = value;
    }

    @JsonCreator
    public static ConnectionHandle fromString(String value) {
        value = value.toLowerCase();
        for (ConnectionHandle handle : ConnectionHandle.values()) {
            if (handle.value.equals(value)) {
                return handle;
            }
        }
        throw CastingException.of(CastingErrorCode.ILLEGAL_HANDLE_ARGS);
    }

    @JsonValue
    public String getValue() {
        return this.value;
    }
}
