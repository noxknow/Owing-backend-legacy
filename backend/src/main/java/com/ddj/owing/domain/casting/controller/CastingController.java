package com.ddj.owing.domain.casting.controller;

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

import com.ddj.owing.domain.casting.model.dto.casting.CastingCoordUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingCreateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingInfoUpdateDto;
import com.ddj.owing.domain.casting.model.dto.casting.CastingRequestDto;
import com.ddj.owing.domain.casting.service.CastingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/casting")
public class CastingController {

	private final CastingService castingService;

    @PostMapping("/generate/coverImage")
    public ResponseEntity<String> generateCharacterImage(@RequestBody CastingRequestDto castingRequestDto) {
        return castingService.generateCharacterImage(castingRequestDto);
    }

	@GetMapping
	public ResponseEntity<List<CastingDto>> getCastingList(@RequestParam Long folderId) {
		List<CastingDto> casting = castingService.getCastingList(folderId);
		return ResponseEntity.ok(casting);
	}

	@GetMapping("/{id}")
	public ResponseEntity<CastingDto> getCastingById(@PathVariable Long id) {
		CastingDto casting = castingService.getCasting(id);
		return ResponseEntity.ok(casting);
	}

	@PostMapping
	public ResponseEntity<CastingDto> createCasting(@RequestBody CastingCreateDto castingCreateDto) {
		CastingDto casting = castingService.createCasting(castingCreateDto);
		return ResponseEntity.ok(casting);
	}

	@PutMapping("/{id}/info")
	public ResponseEntity<CastingDto> updateCastingInfo(@PathVariable Long id,
		@RequestBody CastingInfoUpdateDto castingInfoUpdateDto) {
		CastingDto casting = castingService.updateCastingInfo(id, castingInfoUpdateDto);
		return ResponseEntity.ok(casting);
	}

	@PutMapping("/{id}/coord")
	public ResponseEntity<CastingDto> updateCastingCoord(@PathVariable Long id,
		@RequestBody CastingCoordUpdateDto castingCoordUpdateDto) {
		CastingDto casting = castingService.updateCastingCoord(id, castingCoordUpdateDto);
		return ResponseEntity.ok(casting);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteCasting(@PathVariable Long id) {
		castingService.deleteCasting(id);
		return ResponseEntity.ok().build();
	}

    @PostMapping("/relationship")
    public ResponseEntity<CastingRelationshipDto> createRelationship(CastingConnectionCreateDto castingConnectionCreateDto) {
        CastingRelationshipDto castingRelationshipDto = castingService.createConnection(castingConnectionCreateDto);
        return ResponseEntity.ok(castingRelationshipDto);
    }

    @PutMapping("/relationship/{uuid}")
    public ResponseEntity<CastingRelationshipDto> updateRelationshipName(@PathVariable String uuid, CastingConnectionUpdateDto castingConnectionUpdateDto) {
        CastingRelationshipDto castingRelationshipDto = castingService.updateConnectionName(uuid, castingConnectionUpdateDto);
        return ResponseEntity.ok(castingRelationshipDto);
    }

    @DeleteMapping("/relationship/{uuid}")
    public ResponseEntity<Void> deleteRelationshipByUuid(@PathVariable String uuid) {
        castingService.deleteConnection(uuid);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/graph/{projectId}")
    public ResponseEntity<CastingGraphDto> getGraph(@PathVariable Long projectId) {
        CastingGraphDto graph = castingService.getGraph(projectId);
        return ResponseEntity.ok(graph);
    }
}
