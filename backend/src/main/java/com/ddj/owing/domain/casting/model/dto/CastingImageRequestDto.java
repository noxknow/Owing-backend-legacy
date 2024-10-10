package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingFolder;
import com.ddj.owing.domain.casting.model.CastingNode;
import lombok.Builder;

@Builder
public record CastingImageRequestDto(
        String name,
        Long age,
        String gender,
        String role,
        String detail,
        Integer coordX,
        Integer coordY,
        Long folderId
) {

    public Casting toEntity(CastingFolder castingFolder, Integer position, String imageUrl) {

        return Casting.builder()
                .name(name)
                .age(age)
                .gender(gender)
                .role(role)
                .detail(detail)
                .imageUrl(imageUrl)
                .coordX(coordX)
                .coordY(coordY)
                .castingFolder(castingFolder)
                .position(position)
                .build();
    }

    public CastingNode toNodeEntity(Casting casting) {

        return CastingNode.builder()
                .id(casting.getId())
                .name(name)
                .age(age)
                .gender(gender)
                .role(role)
                .imageUrl(casting.getImageUrl())
                .coordX(coordX)
                .coordY(coordY)
                .build();
    }
}
