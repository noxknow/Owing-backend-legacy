package com.ddj.owing.domain.casting.model.dto;

public record CastingUpdateDto (
        String name,
        Long age,
        String gender,
        String role,
        String detail,
        String imageUrl
) {
}
