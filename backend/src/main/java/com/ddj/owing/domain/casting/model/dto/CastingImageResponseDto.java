package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.Casting;

public record CastingImageResponseDto(
        Long id,
        String presignedUrl
) {

    public static CastingImageResponseDto fromEntity(Casting casting, String presignedUrl) {

        return new CastingImageResponseDto(
                casting.getId(),
                presignedUrl
        );
    }
}
