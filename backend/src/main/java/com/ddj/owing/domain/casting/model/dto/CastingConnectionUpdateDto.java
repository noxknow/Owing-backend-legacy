package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.ConnectionType;

public record CastingConnectionUpdateDto (
        Long fromId,
        Long toId,
        String label,
        ConnectionType connectionType
){
}
