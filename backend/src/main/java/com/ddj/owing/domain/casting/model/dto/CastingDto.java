package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.model.Position;
import lombok.Builder;

@Builder
public record CastingDto(
        Long id,
        String name,
        Long age,
        String gender,
        String role,
        String detail,
        String imageUrl,
        Position position
) {

    public static CastingDto from(CastingNode castingNode) {
        return CastingDto.builder()
                .id(castingNode.getId())
                .name(castingNode.getName())
                .age(castingNode.getAge())
                .gender(castingNode.getGender())
                .role(castingNode.getRole())
                .imageUrl(castingNode.getImageUrl())
                .position(new Position(castingNode.getCoordX(), castingNode.getCoordY()))
                .build();
    }

    public static CastingDto from(Casting casting) {
        return CastingDto.builder()
                .id(casting.getId())
                .name(casting.getName())
                .age(casting.getAge())
                .gender(casting.getGender())
                .role(casting.getRole())
                .detail(casting.getDetail())
                .imageUrl(casting.getCoverImage())
                .position(new Position(casting.getCoordX(), casting.getCoordY()))
                .build();
    }
}
