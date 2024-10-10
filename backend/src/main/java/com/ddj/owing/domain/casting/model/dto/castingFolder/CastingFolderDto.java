package com.ddj.owing.domain.casting.model.dto.castingFolder;

import java.util.Comparator;
import java.util.List;

import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingFolder;
import com.ddj.owing.domain.casting.model.dto.casting.CastingDto;

import lombok.Builder;

@Builder
public record CastingFolderDto(
	Long id,
	String name,
	String description,
	Integer position,
	List<CastingDto> files
) {

	public static CastingFolderDto from(CastingFolder castingFolder) {
		return CastingFolderDto.builder()
			.id(castingFolder.getId())
			.name(castingFolder.getName())
			.description(castingFolder.getDescription())
			.position(castingFolder.getPosition())
			.files(castingFolder.getCastings().stream()
				.sorted(Comparator.comparing(Casting::getPosition))
				.map(CastingDto::from)
				.toList())
			.build();
	}

}
