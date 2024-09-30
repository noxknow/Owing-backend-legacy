package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.UniverseFile;

public record UniverseFileRequestDto(
        String title,
        String description
) {

    public UniverseFile toEntity(String imageUrl) {

        return UniverseFile.builder()
                .title(title)
                .description(description)
                .coverImage(imageUrl)
                .build();
    }
}
