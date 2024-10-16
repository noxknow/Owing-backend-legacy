package com.ddj.owing.domain.universe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddj.owing.domain.universe.model.UniverseFile;
import com.ddj.owing.domain.universe.model.dto.UniverseFileImageRequestDto;
import com.ddj.owing.domain.universe.model.dto.UniverseFileRequestDto;
import com.ddj.owing.domain.universe.model.dto.UniverseFileResponseDto;
import com.ddj.owing.domain.universe.service.UniverseFileService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/universe")
public class UniverseFileController {

	private final UniverseFileService universeFileService;

	@GetMapping("/{universeId}")
	public ResponseEntity<UniverseFile> loadUniverseInfo(@PathVariable("universeId") Long universeId) {
		return universeFileService.loadUniverseInfo(universeId);
	}

	@PostMapping("/image")
	public ResponseEntity<String> generateUniverseImage(@RequestBody UniverseFileRequestDto universeFileRequestDto) {
		return universeFileService.generateUniverseImage(universeFileRequestDto);
	}

	@PostMapping
	public ResponseEntity<String> createUniverseFile(
		@RequestBody UniverseFileImageRequestDto universeFileImageRequestDto) {
		return universeFileService.createUniverseFile(universeFileImageRequestDto);
	}

	@PutMapping("/{universeId}")
	public ResponseEntity<?> updateUniverseFile(@PathVariable("universeId") Long universeId,
		@RequestBody UniverseFileRequestDto universeFileRequestDto) {
		UniverseFileResponseDto dto = universeFileService.updateUniverseFile(universeId, universeFileRequestDto);
		return ResponseEntity.ok(dto);
	}

	@DeleteMapping("/{universeId}")
	public ResponseEntity<?> deleteUniverseFile(@PathVariable("universeId") Long universeId) {
		universeFileService.deleteUniverseFile(universeId);
		return ResponseEntity.ok().build();
	}
}