package com.ddj.owing.casting.controller;

import com.ddj.owing.casting.dto.CastingRequestDto;
import com.ddj.owing.casting.service.CastingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CastingController {

    private final CastingService castingService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateCharacterImage(@RequestBody CastingRequestDto castingRequestDto) {

        return castingService.generateCharacterImage(castingRequestDto);
    }
}
