package com.ddj.owing.domain.universe.model.dto;

public record UniverseFileRequestDto(
	Long folderId,
	String name,
	String description
) {
}
