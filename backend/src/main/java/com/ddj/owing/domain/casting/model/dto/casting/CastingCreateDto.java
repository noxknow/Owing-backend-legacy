package com.ddj.owing.domain.casting.model.dto.casting;

import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingFolder;
import com.ddj.owing.domain.casting.model.CastingNode;

import com.ddj.owing.domain.casting.model.Position;
import lombok.Builder;

@Builder
public record CastingCreateDto(
	String name,
	Long age,
	String gender,
	String role,
	String detail,
	String imageUrl,
	Position position,
	Long folderId
) {

	public Casting toEntity(CastingFolder castingFolder, Integer folderPosition) {
		return Casting.builder()
			.name(name)
			.age(age)
			.gender(gender)
			.role(role)
			.detail(detail)
			.imageUrl(imageUrl)
			.coordX(position.x())
			.coordY(position.y())
			.castingFolder(castingFolder)
			.position(folderPosition)
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
			.coordX(position.x())
			.coordY(position.y())
			.build();
	}
}
