package com.ddj.owing.domain.story.model.dto.storyFolder;

import java.util.Comparator;
import java.util.List;

import com.ddj.owing.domain.story.model.StoryFolder;
import com.ddj.owing.domain.story.model.StoryPlot;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotDto;

import lombok.Builder;

@Builder
public record StoryFolderDto(
	Long id,
	String name,
	String description,
	Integer position,
	List<StoryPlotDto> files
) {

	public static StoryFolderDto from(StoryFolder storyFolder) {
		return StoryFolderDto.builder()
			.id(storyFolder.getId())
			.name(storyFolder.getName())
			.description(storyFolder.getDescription())
			.position(storyFolder.getPosition())
			.files(storyFolder.getStoryPlots().stream()
				.sorted(Comparator.comparing(StoryPlot::getPosition))
				.map(StoryPlotDto::from)
				.toList())
			.build();
	}

}
