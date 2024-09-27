package com.ddj.owing.domain.casting.controller;

import com.ddj.owing.domain.casting.model.dto.CastingCreateDto;
import com.ddj.owing.domain.casting.model.dto.CastingDto;
import com.ddj.owing.domain.casting.model.dto.CastingRequestDto;
import com.ddj.owing.domain.casting.model.dto.CastingUpdateDto;
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

    @PutMapping("/{id}")
    public ResponseEntity<CastingDto> updateCasting(@PathVariable Long id, @RequestBody CastingUpdateDto castingUpdateDto) {
        CastingDto casting = castingService.updateCasting(id, castingUpdateDto);
        return ResponseEntity.ok(casting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCasting(@PathVariable Long id) {
        castingService.deleteCasting(id);
        return ResponseEntity.ok().build();
    }
}
