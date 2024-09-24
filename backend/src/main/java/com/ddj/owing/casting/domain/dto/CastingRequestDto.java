package com.ddj.owing.casting.dto;

import com.ddj.owing.casting.entity.Casting;

public record CastingRequestDto(
        String name,
        Long age,
        String gender,
        String role,
        String detail
) {

    public Casting toEntity(String imageUrl) {

        return Casting.builder()
                .name(this.name())
                .age(this.age())
                .gender(this.gender())
                .role(this.role())
                .detail(this.detail())
                .imageUrl(imageUrl)
                .build();
    }
}