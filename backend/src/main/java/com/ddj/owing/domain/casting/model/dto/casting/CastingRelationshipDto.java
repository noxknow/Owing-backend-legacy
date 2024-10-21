package com.ddj.owing.domain.casting.model.dto.casting;

import com.ddj.owing.domain.casting.model.ConnectionHandle;
import com.ddj.owing.domain.casting.model.ConnectionType;

public record CastingRelationshipDto(
	String uuid,
	Long sourceId,
	Long targetId,
	ConnectionType type,
	ConnectionHandle sourceHandle,
	ConnectionHandle targetHandle
) {
}
