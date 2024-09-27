package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.CastingNode;
import lombok.Builder;

@Builder
public record CastingCreateDto(
        String name,
        Long age,
        String gender,
        String role,
        String detail,
        String imageUrl
) {

    public Casting toEntity() {
        return Casting.builder()
                .name(name)
                .age(age)
                .gender(gender)
                .role(role)
                .detail(detail)
                .coverImage(imageUrl)
                .build();
    }

    public CastingNode toNodeEntity(Casting savedCasting) {
        return CastingNode.builder()
                .id(savedCasting.getId())
                .name(name)
                .age(age)
                .gender(gender)
                .role(role)
                .detail(detail)
                .imageUrl(imageUrl)
                .build();
    }
}
