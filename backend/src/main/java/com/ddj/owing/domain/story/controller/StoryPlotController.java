package com.ddj.owing.domain.story.controller;

import java.util.List;

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

import com.ddj.owing.domain.story.model.dto.StoryPlotAppearedCastDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotAppearedCastCreateDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotCreateDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotPositionUpdateDto;
import com.ddj.owing.domain.story.model.dto.storyPlot.StoryPlotUpdateDto;
import com.ddj.owing.domain.story.service.StoryPlotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storyPlot")
@RequiredArgsConstructor
public class StoryPlotController {
	private final StoryPlotService storyPlotService;

	@GetMapping
	public ResponseEntity<List<StoryPlotDto>> getStoryPlotList(@RequestParam Long storyFolderId) {
		List<StoryPlotDto> stories = storyPlotService.getStoryPlotList(storyFolderId);
		return ResponseEntity.ok(stories);
	}

	@GetMapping("/{storyPlotId}")
	public ResponseEntity<StoryPlotDto> getStoryPlotById(@PathVariable Long storyPlotId) {
		StoryPlotDto storyFolder = storyPlotService.getStoryPlot(storyPlotId);
		return ResponseEntity.ok(storyFolder);
	}

	@PostMapping
	public ResponseEntity<StoryPlotDto> createStoryPlot(/*@Valid*/ @RequestBody StoryPlotCreateDto storyFolderDto) {
		StoryPlotDto createdStory = storyPlotService.createStoryPlot(storyFolderDto);
		return ResponseEntity.ok(createdStory);
	}

	@PutMapping("/{storyPlotId}")
	public ResponseEntity<StoryPlotDto> updateStoryPlot(@PathVariable Long storyPlotId,
		/*@Valid*/ @RequestBody StoryPlotUpdateDto storyPlotUpdateDto) {
		StoryPlotDto updatedStory = storyPlotService.updateStoryPlot(storyPlotId, storyPlotUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@PatchMapping("/{storyPlotId}")
	public ResponseEntity<StoryPlotDto> updateStoryPlotPosition(@PathVariable Long storyPlotId,
		@RequestBody StoryPlotPositionUpdateDto storyPlotPositionUpdateDto) {
		StoryPlotDto updatedStory = storyPlotService.updateStoryPlotPosition(storyPlotId, storyPlotPositionUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@DeleteMapping("/{storyPlotId}")
	public ResponseEntity<Void> deleteStoryPlot(@PathVariable Long storyPlotId) {
		storyPlotService.deleteStoryPlot(storyPlotId);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/{storyPlotId}/appearedCast")
	public ResponseEntity<List<StoryPlotAppearedCastDto>> createAppearedCast(
		@PathVariable Long storyPlotId,
		@RequestBody StoryPlotAppearedCastCreateDto storyPlotAppearedCastCreateDto
	) {
		List<StoryPlotAppearedCastDto> appearedCastList = storyPlotService.registerCasts(storyPlotId,
			storyPlotAppearedCastCreateDto);
		return ResponseEntity.ok(appearedCastList);
	}

	@DeleteMapping("/{storyPlotId}/appearedCast/{castId}")
	public ResponseEntity<Void> deleteAppearedCast(@PathVariable Long storyPlotId, @PathVariable Long castId) {
		storyPlotService.deleteAppearedCast(storyPlotId, castId);
		return ResponseEntity.ok().build();
	}
}
