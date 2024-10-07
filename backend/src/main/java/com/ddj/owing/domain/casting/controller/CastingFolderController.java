package com.ddj.owing.domain.casting.controller;

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

import com.ddj.owing.domain.casting.model.dto.castingFolder.CastingFolderCreateDto;
import com.ddj.owing.domain.casting.model.dto.castingFolder.CastingFolderDto;
import com.ddj.owing.domain.casting.model.dto.castingFolder.CastingFolderPositionUpdateDto;
import com.ddj.owing.domain.casting.model.dto.castingFolder.CastingFolderUpdateDto;
import com.ddj.owing.domain.casting.service.CastingFolderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/castingFolder")
@RequiredArgsConstructor
public class CastingFolderController {
	private final CastingFolderService castingFolderService;

	@GetMapping
	public ResponseEntity<List<CastingFolderDto>> getCastingFolderList(@RequestParam Long projectId) {
		List<CastingFolderDto> stories = castingFolderService.getCastingFolderList(projectId);
		return ResponseEntity.ok(stories);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CastingFolderDto> getCastingFolder(@PathVariable Long id) {
		CastingFolderDto castingFolder = castingFolderService.getCastingFolder(id);
		return ResponseEntity.ok(castingFolder);
	}

	@PostMapping
	public ResponseEntity<CastingFolderDto> createCastingFolder(/*@Valid*/
		@RequestBody CastingFolderCreateDto castingFolderDto) {
		CastingFolderDto createdCasting = castingFolderService.createCastingFolder(castingFolderDto);
		return ResponseEntity.ok(createdCasting);
	}

	@PutMapping("/{id}")
	public ResponseEntity<CastingFolderDto> updateCastingFolder(@PathVariable Long id,
		/*@Valid*/ @RequestBody CastingFolderUpdateDto castingFolderUpdateDto) {
		CastingFolderDto updatedCasting = castingFolderService.updateCastingFolder(id, castingFolderUpdateDto);
		return ResponseEntity.ok(updatedCasting);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<CastingFolderDto> updateCastingFolderPosition(@PathVariable Long id,
		@RequestBody CastingFolderPositionUpdateDto castingFolderPositionUpdateDto) {
		CastingFolderDto updatedCasting = castingFolderService.updateCastingFolderPosition(id,
			castingFolderPositionUpdateDto);
		return ResponseEntity.ok(updatedCasting);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCastingFolder(@PathVariable Long id) {
		castingFolderService.deleteCastingFolder(id);
		return ResponseEntity.noContent().build();
	}
}
