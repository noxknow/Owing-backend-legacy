package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.ConnectionType;

public record CastingRelationshipDto (
        String uuid,
        Long fromId,
        Long toId,
        ConnectionType connectionType
) {
}
