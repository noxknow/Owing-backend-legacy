package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.UniverseFile;
import com.ddj.owing.domain.universe.model.UniverseFolder;

public record UniverseFileImageRequestDto(
        Long universeFolderId,
        String title,
        String description,
        String imageUrl
) {

    public UniverseFile toEntity(String imageUrl, UniverseFolder universeFolder) {

        return UniverseFile.builder()
                .universeFolder(universeFolder)
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .build();
    }
}
