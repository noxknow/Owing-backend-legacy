package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.ConnectionType;

public record CastingRelationshipDto (
        Long id,
        Long fromId,
        Long toId,
        ConnectionType connectionType
) {
}
