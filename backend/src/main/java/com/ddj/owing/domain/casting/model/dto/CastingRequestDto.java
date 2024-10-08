package com.ddj.owing.domain.casting.model.dto;

public record CastingRequestDto(
        String name,
        Long age,
        String gender,
        String role,
        String detail
) {
}