package com.ddj.owing.domain.story.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ddj.owing.domain.story.model.dto.storyPage.StoryPageDto;
import com.ddj.owing.domain.story.service.StoryPageService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storyPage")
@RequiredArgsConstructor
public class StoryPageController {
	private final StoryPageService storyPageService;

	@GetMapping
	public ResponseEntity<StoryPageDto> getStoryPage(@RequestParam Long storyPlotId) {
		StoryPageDto storyPage = storyPageService.getStoryPage(storyPlotId);
		return ResponseEntity.ok(storyPage);
	}

	@PostMapping
	public ResponseEntity<StoryPageDto> createStoryBlock(@RequestBody StoryPageDto storyPageDto) {
		StoryPageDto createdStory = storyPageService.createStoryPage(storyPageDto);
		return ResponseEntity.ok(createdStory);
	}

	@PutMapping
	public ResponseEntity<StoryPageDto> updateStoryBlock(@RequestBody StoryPageDto storyPageDto) {
		StoryPageDto updatedStory = storyPageService.updateStoryPage(storyPageDto);
		return ResponseEntity.ok(updatedStory);
	}

}
