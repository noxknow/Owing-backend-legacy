package com.ddj.owing.domain.story.controller;

import java.util.List;
import java.util.Set;

import com.ddj.owing.domain.story.model.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddj.owing.domain.story.service.StoryPlotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storyPlot")
@RequiredArgsConstructor
public class StoryPlotController {
	private final StoryPlotService storyPlotService;

	@GetMapping
	public ResponseEntity<List<StoryPlotDto>> getStoryPlotList(@RequestParam Long folderId) {
		List<StoryPlotDto> stories = storyPlotService.getStoryPlotList(folderId);
		return ResponseEntity.ok(stories);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StoryPlotDto> getStoryPlotById(@PathVariable Long id) {
		StoryPlotDto storyFolder = storyPlotService.getStoryPlot(id);
		return ResponseEntity.ok(storyFolder);
	}

	@PostMapping
	public ResponseEntity<StoryPlotDto> createStoryPlot(/*@Valid*/ @RequestBody StoryPlotCreateDto storyFolderDto) {
		StoryPlotDto createdStory = storyPlotService.createStoryPlot(storyFolderDto);
		return ResponseEntity.ok(createdStory);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StoryPlotDto> updateStoryPlot(@PathVariable Long id,
		/*@Valid*/ @RequestBody StoryPlotUpdateDto storyPlotUpdateDto) {
		StoryPlotDto updatedStory = storyPlotService.updateStoryPlot(id, storyPlotUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<StoryPlotDto> updateStoryPlotPosition(@PathVariable Long id,
		@RequestBody StoryPlotPositionUpdateDto storyPlotPositionUpdateDto) {
		StoryPlotDto updatedStory = storyPlotService.updateStoryPlotPosition(id, storyPlotPositionUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStoryPlot(@PathVariable Long id) {
		storyPlotService.deleteStoryPlot(id);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{id}/appeared-cast")
	public ResponseEntity<List<StoryPlotAppearedCastDto>> createAppearedCast(
			@PathVariable Long id,
			@RequestBody StoryPlotAppearedCastCreateDto storyPlotAppearedCastCreateDto
	) {
		List<StoryPlotAppearedCastDto> appearedCastList = storyPlotService.registerCasts(id, storyPlotAppearedCastCreateDto);
		return ResponseEntity.ok(appearedCastList);
	}
}
