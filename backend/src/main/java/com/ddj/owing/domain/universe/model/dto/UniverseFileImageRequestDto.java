package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.UniverseFile;
import com.ddj.owing.domain.universe.model.UniverseFolder;

public record UniverseFileImageRequestDto(
        Long universeFolderId,
        String title,
        String description
) {

    public UniverseFile toEntity(UniverseFolder universeFolder, String imageUrl) {

        return UniverseFile.builder()
                .universeFolder(universeFolder)
                .title(title)
                .description(description)
                .imageUrl(imageUrl)
                .build();
    }
}
