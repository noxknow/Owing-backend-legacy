package com.ddj.owing.domain.storyFolder.controller;

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

import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderCreateDto;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderDto;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderPositionUpdateDto;
import com.ddj.owing.domain.storyFolder.service.StoryFolderService;
import com.ddj.owing.domain.storyFolder.model.dto.StoryFolderUpdateDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/storyFolder")
@RequiredArgsConstructor
public class StoryFolderController {
	private final StoryFolderService storyFolderService;

	@GetMapping
	public ResponseEntity<List<StoryFolderDto>> getStoryFolderList(@RequestParam Long projectId) {
		List<StoryFolderDto> stories = storyFolderService.getStoryFolderList(projectId);
		return ResponseEntity.ok(stories);
	}

	@GetMapping("/{id}")
	public ResponseEntity<StoryFolderDto> getStoryFolder(@PathVariable Long id) {
		StoryFolderDto storyFolder = storyFolderService.getStoryFolder(id);
		return ResponseEntity.ok(storyFolder);
	}

	@PostMapping
	public ResponseEntity<StoryFolderDto> createStoryFolder(/*@Valid*/
		@RequestBody StoryFolderCreateDto storyFolderDto) {
		StoryFolderDto createdStory = storyFolderService.createStoryFolder(storyFolderDto);
		return ResponseEntity.ok(createdStory);
	}

	@PutMapping("/{id}")
	public ResponseEntity<StoryFolderDto> updateStoryFolder(@PathVariable Long id,
		/*@Valid*/ @RequestBody StoryFolderUpdateDto storyFolderUpdateDto) {
		StoryFolderDto updatedStory = storyFolderService.updateStoryFolder(id, storyFolderUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<StoryFolderDto> updateStoryFolderPosition(@PathVariable Long id,
		@RequestBody StoryFolderPositionUpdateDto storyFolderPositionUpdateDto) {
		StoryFolderDto updatedStory = storyFolderService.updateStoryFolderPosition(id, storyFolderPositionUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteStoryFolder(@PathVariable Long id) {
		storyFolderService.deleteStoryFolder(id);
		return ResponseEntity.noContent().build();
	}
}
