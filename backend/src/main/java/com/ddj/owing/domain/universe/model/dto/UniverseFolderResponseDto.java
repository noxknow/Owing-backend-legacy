package com.ddj.owing.domain.universe.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.ddj.owing.domain.universe.model.UniverseFolder;

public record UniverseFolderResponseDto(
	Long id,
	String name,
	String description,
	List<UniverseFileResponseDto> files
) {

	public static UniverseFolderResponseDto fromEntity(UniverseFolder universeFolder) {

		List<UniverseFileResponseDto> files = universeFolder.getUniverseFiles().stream()
			.map(UniverseFileResponseDto::fromEntity)
			.collect(Collectors.toList());

		return new UniverseFolderResponseDto(
			universeFolder.getId(),
			universeFolder.getName(),
			universeFolder.getDescription(),
			files
		);
	}
}
