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

import com.ddj.owing.domain.story.model.dto.storyFolder.StoryFolderCreateDto;
import com.ddj.owing.domain.story.model.dto.storyFolder.StoryFolderDto;
import com.ddj.owing.domain.story.model.dto.storyFolder.StoryFolderPositionUpdateDto;
import com.ddj.owing.domain.story.model.dto.storyFolder.StoryFolderUpdateDto;
import com.ddj.owing.domain.story.service.StoryFolderService;

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

	@GetMapping("/{storyFolderId}")
	public ResponseEntity<StoryFolderDto> getStoryFolder(@PathVariable Long storyFolderId) {
		StoryFolderDto storyFolder = storyFolderService.getStoryFolder(storyFolderId);
		return ResponseEntity.ok(storyFolder);
	}

	@PostMapping
	public ResponseEntity<StoryFolderDto> createStoryFolder(/*@Valid*/
		@RequestBody StoryFolderCreateDto storyFolderDto) {
		StoryFolderDto createdStory = storyFolderService.createStoryFolder(storyFolderDto);
		return ResponseEntity.ok(createdStory);
	}

	@PutMapping("/{storyFolderId}")
	public ResponseEntity<StoryFolderDto> updateStoryFolder(@PathVariable Long storyFolderId,
		/*@Valid*/ @RequestBody StoryFolderUpdateDto storyFolderUpdateDto) {
		StoryFolderDto updatedStory = storyFolderService.updateStoryFolder(storyFolderId, storyFolderUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@PatchMapping("/{storyFolderId}")
	public ResponseEntity<StoryFolderDto> updateStoryFolderPosition(@PathVariable Long storyFolderId,
		@RequestBody StoryFolderPositionUpdateDto storyFolderPositionUpdateDto) {
		StoryFolderDto updatedStory = storyFolderService.updateStoryFolderPosition(storyFolderId,
			storyFolderPositionUpdateDto);
		return ResponseEntity.ok(updatedStory);
	}

	@DeleteMapping("/{storyFolderId}")
	public ResponseEntity<Void> deleteStoryFolder(@PathVariable Long storyFolderId) {
		storyFolderService.deleteStoryFolder(storyFolderId);
		return ResponseEntity.noContent().build();
	}
}
