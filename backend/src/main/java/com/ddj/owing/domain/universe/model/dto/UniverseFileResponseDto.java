package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.UniverseFile;

public record UniverseFileResponseDto(
        Long id,
        String name,
        String description,
        String imageUrl
) {

    public static UniverseFileResponseDto fromEntity(UniverseFile file) {

        return new UniverseFileResponseDto(
                file.getId(),
                file.getName(),
                file.getDescription(),
                file.getImageUrl()
        );
    }
}
