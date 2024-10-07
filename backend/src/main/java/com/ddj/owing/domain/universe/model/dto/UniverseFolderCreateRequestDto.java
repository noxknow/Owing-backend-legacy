package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.UniverseFolder;

public record UniverseFolderCreateRequestDto(
        String title,
        String description
) {

    public UniverseFolder toEntity() {

        return UniverseFolder.builder()
                .title(title)
                .description(description)
                .build();
    }
}
