package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.UniverseFile;
import com.ddj.owing.domain.universe.model.UniverseFolder;

public record UniverseFileImageRequestDto(
	Long folderId,
	String name,
	String description
) {

	public UniverseFile toEntity(UniverseFolder universeFolder, String imageUrl) {

		return UniverseFile.builder()
			.universeFolder(universeFolder)
			.name(name)
			.description(description)
			.imageUrl(imageUrl)
			.build();
	}
}
