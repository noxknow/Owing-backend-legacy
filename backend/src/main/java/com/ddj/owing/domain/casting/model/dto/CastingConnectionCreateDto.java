package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.ConnectionType;

public record CastingConnectionCreateDto(
        String uuid,
        Long sourceId,
        Long targetId,
        String label,
        ConnectionType connectionType,
        String sourceHandleStr,
        String targetHandleStr
) {
}
