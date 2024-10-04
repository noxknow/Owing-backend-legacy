package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.ConnectionHandle;
import com.ddj.owing.domain.casting.model.ConnectionType;

public record CastingRelationshipInfoDto(
        String uuid,
        String type,
        String label,
        Long sourceId,
        Long targetId,
        ConnectionHandle sourceHandle,
        ConnectionHandle targetHandle
) {
    public CastingRelationshipInfoDto {
        type = ConnectionType.of(type).name();
    }
}
