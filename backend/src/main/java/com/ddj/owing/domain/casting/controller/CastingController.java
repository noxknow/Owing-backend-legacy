package com.ddj.owing.domain.casting.controller;

import com.ddj.owing.domain.casting.model.dto.CastingDto;
import com.ddj.owing.domain.casting.model.dto.CastingRequestDto;
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
    public ResponseEntity<CastingDto> getCastingById(@PathVariable(value = "id") Long id) {
        CastingDto casting = castingService.getCasting(id);
        return ResponseEntity.ok(casting);
    }
}
