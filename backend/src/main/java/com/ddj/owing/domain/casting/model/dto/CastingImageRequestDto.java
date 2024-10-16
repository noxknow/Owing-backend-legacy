package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingFolder;
import com.ddj.owing.domain.casting.model.CastingNode;
import com.ddj.owing.domain.casting.model.Position;
import lombok.Builder;

@Builder
public record CastingImageRequestDto(
        String name,
        Long age,
        String gender,
        String role,
        String detail,
        Position position,
        Long folderId
) {

    public Casting toEntity(CastingFolder castingFolder, Integer folderPosition, String imageUrl) {

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

    public CastingNode toNodeEntity(Casting casting) {

        return CastingNode.builder()
                .id(casting.getId())
                .name(name)
                .age(age)
                .gender(gender)
                .role(role)
                .imageUrl(casting.getImageUrl())
                .coordX(position.x())
                .coordY(position.y())
                .build();
    }
}
