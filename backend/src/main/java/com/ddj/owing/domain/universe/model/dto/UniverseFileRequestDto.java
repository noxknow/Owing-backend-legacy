package com.ddj.owing.domain.universe.model.dto;

public record UniverseFileRequestDto(
        Long folderId,
        String title,
        String description
) {
}
