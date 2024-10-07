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

import com.ddj.owing.domain.story.model.dto.storyBlock.StoryBlockCreateDto;
import com.ddj.owing.domain.story.model.dto.storyBlock.StoryBlockDto;
import com.ddj.owing.domain.story.model.dto.storyBlock.StoryBlockPositionUpdateDto;
import com.ddj.owing.domain.story.model.dto.storyBlock.StoryBlockUpdateDto;
import com.ddj.owing.domain.story.service.StoryBlockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storyBlock")
@RequiredArgsConstructor
public class StoryBlockController {
	private final StoryBlockService storyBlockService;

	@GetMapping
	public ResponseEntity<List<StoryBlockDto>> getStoryBlockList(@RequestParam Long plotId) {
		List<StoryBlockDto> stories = storyBlockService.getStoryBlockList(plotId);
		return ResponseEntity.ok(stories);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StoryBlockDto> getStoryBlock(@PathVariable Long id) {
		StoryBlockDto storyFolder = storyBlockService.getStoryBlock(id);
		return ResponseEntity.ok(storyFolder);
	}

	@PostMapping
	public ResponseEntity<StoryBlockDto> createStoryBlock(/*@Valid*/ @RequestBody StoryBlockCreateDto storyFolderDto) {
		StoryBlockDto createdStory = storyBlockService.createStoryBlock(storyFolderDto);
		return ResponseEntity.ok(createdStory);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StoryBlockDto> updateStoryBlock(@PathVariable Long id,
		/*@Valid*/ @RequestBody StoryBlockUpdateDto storyBlockUpdateDto) {
		StoryBlockDto updatedStory = storyBlockService.updateStoryBlock(id, storyBlockUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<StoryBlockDto> updateStoryBlockPosition(@PathVariable Long id,
		@RequestBody StoryBlockPositionUpdateDto storyBlockPositionUpdateDto) {
		StoryBlockDto updatedStory = storyBlockService.updateStoryBlockPosition(id, storyBlockPositionUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStoryBlock(@PathVariable Long id) {
		storyBlockService.deleteStoryBlock(id);
		return ResponseEntity.noContent().build();
	}
}
