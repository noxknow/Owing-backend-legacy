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
	public ResponseEntity<List<StoryBlockDto>> getStoryBlockList(@RequestParam Long storyPlotId) {
		List<StoryBlockDto> stories = storyBlockService.getStoryBlockList(storyPlotId);
		return ResponseEntity.ok(stories);
	}

	@GetMapping("/{storyBlockId}")
	public ResponseEntity<StoryBlockDto> getStoryBlock(@PathVariable Long storyBlockId) {
		StoryBlockDto storyFolder = storyBlockService.getStoryBlock(storyBlockId);
		return ResponseEntity.ok(storyFolder);
	}

	@PostMapping
	public ResponseEntity<StoryBlockDto> createStoryBlock(/*@Valid*/ @RequestBody StoryBlockCreateDto storyFolderDto) {
		StoryBlockDto createdStory = storyBlockService.createStoryBlock(storyFolderDto);
		return ResponseEntity.ok(createdStory);
	}

	@PutMapping("/{storyBlockId}")
	public ResponseEntity<StoryBlockDto> updateStoryBlock(@PathVariable Long storyBlockId,
		/*@Valid*/ @RequestBody StoryBlockUpdateDto storyBlockUpdateDto) {
		StoryBlockDto updatedStory = storyBlockService.updateStoryBlock(storyBlockId, storyBlockUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@PatchMapping("/{storyBlockId}")
	public ResponseEntity<StoryBlockDto> updateStoryBlockPosition(@PathVariable Long storyBlockId,
		@RequestBody StoryBlockPositionUpdateDto storyBlockPositionUpdateDto) {
		StoryBlockDto updatedStory = storyBlockService.updateStoryBlockPosition(storyBlockId,
			storyBlockPositionUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@DeleteMapping("/{storyBlockId}")
	public ResponseEntity<Void> deleteStoryBlock(@PathVariable Long storyBlockId) {
		storyBlockService.deleteStoryBlock(storyBlockId);
		return ResponseEntity.noContent().build();
	}
}
