package com.ddj.owing.domain.casting.model.dto.casting;

import java.util.List;

import com.ddj.owing.domain.casting.model.dto.CastingRelationshipInfoDto;

public record CastingGraphDto(
	List<CastingNodeDto> nodes,
	List<CastingRelationshipInfoDto> edges
) {
}
