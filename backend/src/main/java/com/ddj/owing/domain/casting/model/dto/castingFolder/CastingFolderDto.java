package com.ddj.owing.domain.casting.model.dto.castingFolder;

import com.ddj.owing.domain.casting.model.CastingFolder;

import lombok.Builder;

@Builder
public record CastingFolderDto(
	Long id,
	String name,
	String description,
	Integer position
) {

	public static CastingFolderDto from(CastingFolder castingFolder) {
		return CastingFolderDto.builder()
			.id(castingFolder.getId())
			.name(castingFolder.getName())
			.description(castingFolder.getDescription())
			.position(castingFolder.getPosition())
			.build();
	}

}
