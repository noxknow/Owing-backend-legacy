package com.ddj.owing.domain.casting.controller;

import com.ddj.owing.domain.casting.model.dto.*;
import com.ddj.owing.domain.casting.service.CastingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/casting")
public class CastingController {

    private final CastingService castingService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateCharacterImage(@RequestBody CastingRequestDto castingRequestDto) {
        return castingService.generateCharacterImage(castingRequestDto);
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
    public ResponseEntity<CastingDto> updateCastingInfo(@PathVariable Long id, @RequestBody CastingInfoUpdateDto castingInfoUpdateDto) {
        CastingDto casting = castingService.updateCastingInfo(id, castingInfoUpdateDto);
        return ResponseEntity.ok(casting);
    }

    @PutMapping("/{id}/coord")
    public ResponseEntity<CastingDto> updateCastingCoord(@PathVariable Long id, @RequestBody CastingCoordUpdateDto castingCoordUpdateDto) {
        CastingDto casting = castingService.updateCastingCoord(id, castingCoordUpdateDto);
        return ResponseEntity.ok(casting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCasting(@PathVariable Long id) {
        castingService.deleteCasting(id);
        return ResponseEntity.ok().build();
    }
}
