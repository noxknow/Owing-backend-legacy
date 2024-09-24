package com.ddj.owing.domain.universe.controller;

import com.ddj.owing.domain.universe.model.dto.UniverseRequestDto;
import com.ddj.owing.domain.universe.service.UniverseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UniverseController {

    private final UniverseService universeService;

    @PostMapping("/universe/generate")
    public ResponseEntity<String> generateUniverseImage(@RequestBody UniverseRequestDto universeRequestDto) {
        return universeService.generateUniverseImage(universeRequestDto);
    }
}
