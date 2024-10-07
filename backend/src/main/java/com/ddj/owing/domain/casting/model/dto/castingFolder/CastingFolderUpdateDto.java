package com.ddj.owing.domain.casting.model.dto.castingFolder;

import jakarta.validation.constraints.NotBlank;

public record CastingFolderUpdateDto(
	@NotBlank String name,
	String description
) {
}