package com.ddj.owing.domain.storyPlot.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddj.owing.domain.storyPlot.model.dto.StoryPlotCreateDto;
import com.ddj.owing.domain.storyPlot.model.dto.StoryPlotDto;
import com.ddj.owing.domain.storyPlot.model.dto.StoryPlotUpdateDto;
import com.ddj.owing.domain.storyPlot.service.StoryPlotService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storyPlot")
@RequiredArgsConstructor
public class StoryPlotController {
	private final StoryPlotService storyPlotService;

	@GetMapping
	public ResponseEntity<List<StoryPlotDto>> getAllStories(@RequestParam Long folderId) {
		List<StoryPlotDto> stories = storyPlotService.getStoryPlotList(folderId);
		return ResponseEntity.ok(stories);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StoryPlotDto> getStoryById(@PathVariable Long id) {
		StoryPlotDto storyFolder = storyPlotService.getStoryPlot(id);
		return ResponseEntity.ok(storyFolder);
	}

	@PostMapping
	public ResponseEntity<StoryPlotDto> createStory(@RequestBody StoryPlotCreateDto storyFolderDto) {
		StoryPlotDto createdStory = storyPlotService.createStoryPlot(storyFolderDto);
		return ResponseEntity.ok(createdStory);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StoryPlotDto> updateStory(@PathVariable Long id,
		@RequestBody StoryPlotUpdateDto storyPlotUpdateDto) {
		StoryPlotDto updatedStory = storyPlotService.updateStoryPlot(id, storyPlotUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStory(@PathVariable Long id) {
		storyPlotService.deleteStoryPlot(id);
		return ResponseEntity.ok().build();
	}
}
