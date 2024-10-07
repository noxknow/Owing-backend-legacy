package com.ddj.owing.domain.casting.model.dto.casting;

import jakarta.validation.constraints.NotNull;

public record CastingPositionUpdateDto(
	@NotNull Integer position,
	@NotNull Long folderId
) {
}
