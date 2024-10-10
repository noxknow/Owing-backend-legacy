package com.ddj.owing.domain.universe.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddj.owing.domain.universe.model.dto.UniverseFolderCreateRequestDto;
import com.ddj.owing.domain.universe.model.dto.UniverseFolderResponseDto;
import com.ddj.owing.domain.universe.model.dto.UniverseFolderUpdateRequestDto;
import com.ddj.owing.domain.universe.service.UniverseFolderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/universeFolder")
public class UniverseFolderController {

	private final UniverseFolderService universeFolderService;

	@PostMapping
	public ResponseEntity<Void> createFolder(
		@RequestBody UniverseFolderCreateRequestDto universeFolderCreateRequestDto) {
		return universeFolderService.createFolder(universeFolderCreateRequestDto);
	}

	@GetMapping
	public ResponseEntity<List<UniverseFolderResponseDto>> getAllFolders() {
		return universeFolderService.getAllFolders();
	}

	//    @GetMapping("/{id}")
	//    public ResponseEntity<UniverseFolderResponseDto> getFolderById(@PathVariable("id") Long id) {
	//        return universeFolderService.getFolderById(id);
	//    }

	@DeleteMapping("/{universeFolderId}")
	public ResponseEntity<Void> deleteFolder(@PathVariable("universeFolderId") Long universeFolderId) {
		return universeFolderService.deleteFolder(universeFolderId);
	}

	@PutMapping("/{universeFolderId}")
	public ResponseEntity<Void> updateFolder(@PathVariable("universeFolderId") Long universeFolderId,
		@RequestBody UniverseFolderUpdateRequestDto universeFolderUpdateRequestDto) {
		return universeFolderService.updateFolder(universeFolderId, universeFolderUpdateRequestDto);
	}
}
