package com.ddj.owing.domain.universe.model.dto;

public record UniverseFileRequestDto(
        Long universeFolderId,
        String title,
        String description
) {
}
