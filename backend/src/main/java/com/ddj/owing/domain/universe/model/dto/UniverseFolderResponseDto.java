package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.UniverseFile;
import com.ddj.owing.domain.universe.model.UniverseFolder;

import java.util.List;
import java.util.stream.Collectors;

public record UniverseFolderResponseDto(
        Long id,
        String title,
        String description,
        List<UniverseFileResponseDto> universeFiles
) {

    public static UniverseFolderResponseDto fromEntity(UniverseFolder universeFolder) {

        List<UniverseFileResponseDto> files = universeFolder.getUniverseFiles().stream()
                .map(UniverseFileResponseDto::fromEntity)
                .collect(Collectors.toList());

        return new UniverseFolderResponseDto(
                universeFolder.getId(),
                universeFolder.getTitle(),
                universeFolder.getDescription(),
                files
        );
    }
}
