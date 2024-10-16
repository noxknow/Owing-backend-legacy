package com.ddj.owing.domain.casting.model.dto.casting;

import com.ddj.owing.domain.casting.model.ConnectionHandle;
import com.ddj.owing.domain.casting.model.ConnectionType;

public record CastingConnectionUpdateDto(
	Long sourceId,
	Long targetId,
	String label,
	ConnectionType connectionType,
	ConnectionHandle sourceHandle,
	ConnectionHandle targetHandle
) {
}
