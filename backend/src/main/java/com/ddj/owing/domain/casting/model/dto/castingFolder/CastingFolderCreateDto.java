package com.ddj.owing.domain.casting.model.dto.castingFolder;

import com.ddj.owing.domain.casting.model.CastingFolder;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CastingFolderCreateDto(
	@NotNull Long projectId,
	@NotBlank String name,
	String description
) {

	public CastingFolder toEntity(Integer position) {
		return CastingFolder.builder()
			.projectId(projectId)
			.name(name)
			.description(description)
			.position(position)
			.build();
	}
}