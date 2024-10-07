package com.ddj.owing.domain.casting.model.dto.casting;

import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingFolder;
import com.ddj.owing.domain.casting.model.CastingNode;

import lombok.Builder;

@Builder
public record CastingCreateDto(
	String name,
	Long age,
	String gender,
	String role,
	String detail,
	String imageUrl,
	Integer coordX,
	Integer coordY,
	Long folderId
) {

	public Casting toEntity(CastingFolder castingFolder, Integer position) {
		return Casting.builder()
			.name(name)
			.age(age)
			.gender(gender)
			.role(role)
			.detail(detail)
			.coverImage(imageUrl)
			.coordX(coordX)
			.coordY(coordY)
			.castingFolder(castingFolder)
			.position(position)
			.build();
	}

	public CastingNode toNodeEntity(Casting savedCasting) {
		return CastingNode.builder()
			.id(savedCasting.getId())
			.name(name)
			.age(age)
			.gender(gender)
			.role(role)
			.imageUrl(imageUrl)
			.coordX(coordX)
			.coordY(coordY)
			.build();
	}
}
