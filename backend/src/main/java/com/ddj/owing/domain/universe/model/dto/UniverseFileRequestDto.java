package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.UniverseFile;
import com.ddj.owing.domain.universe.model.UniverseFolder;

public record UniverseFileRequestDto(
        Long universeFolderId,
        String title,
        String description
) {

    public UniverseFile toEntity(String imageUrl, UniverseFolder universeFolder) {

        return UniverseFile.builder()
                .universeFolder(universeFolder)
                .title(title)
                .description(description)
                .coverImage(imageUrl)
                .build();
    }
}
