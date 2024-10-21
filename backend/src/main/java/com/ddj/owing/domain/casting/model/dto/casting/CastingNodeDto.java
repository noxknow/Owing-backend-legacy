package com.ddj.owing.domain.casting.model.dto.casting;

import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.model.Position;
import lombok.Builder;

@Builder
public record CastingNodeDto(
	Long id,
	String name,
	String role,
	String imageUrl,
	Position position
) {

	public static CastingNodeDto from(CastingNode castingNode) {
		return CastingNodeDto.builder()
			.id(castingNode.getId())
			.name(castingNode.getName())
			.role(castingNode.getRole())
			.imageUrl(castingNode.getImageUrl())
			.position(new Position(castingNode.getCoordX(), castingNode.getCoordY()))
			.build();
	}
}
