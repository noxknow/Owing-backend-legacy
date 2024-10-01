package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.ConnectionType;

public record CastingConnectionCreateDto(
        Long fromId,
        Long toId,
        String name,
        ConnectionType connectionType
) {
}
