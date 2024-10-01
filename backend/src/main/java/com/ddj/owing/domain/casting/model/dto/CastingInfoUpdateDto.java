package com.ddj.owing.domain.casting.model.dto;

public record CastingInfoUpdateDto(
        String name,
        Long age,
        String gender,
        String role,
        String detail,
        String imageUrl
) {
}
