package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.UniverseFolder;

import java.util.List;
import java.util.stream.Collectors;

public record UniverseFolderResponseDto(
        Long id,
        String title,
        String description,
        List<Long> universeFileIds
) {

    public static UniverseFolderResponseDto fromEntity(UniverseFolder universeFolder) {

        List<Long> fileIds = universeFolder.getUniverseFiles()
                .stream()
                .map(file -> file.getId())
                .collect(Collectors.toList());

        return new UniverseFolderResponseDto(
                universeFolder.getId(),
                universeFolder.getTitle(),
                universeFolder.getDescription(),
                fileIds
        );
    }
}
