package com.ddj.owing.domain.casting.model.dto;

import java.util.List;

public record CastingGraphDto(
        List<CastingDto> nodes,
        List<CastingRelationshipInfoDto> edges
) {
}
