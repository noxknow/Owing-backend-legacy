package com.ddj.owing.domain.universe.controller;

import com.ddj.owing.domain.universe.model.UniverseFile;
import com.ddj.owing.domain.universe.model.dto.UniverseFileImageRequestDto;
import com.ddj.owing.domain.universe.model.dto.UniverseFileRequestDto;
import com.ddj.owing.domain.universe.service.UniverseFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/universe/file")
public class UniverseFileController {

    private final UniverseFileService universeFileService;

    @GetMapping("/info/{id}")
    public ResponseEntity<UniverseFile> loadUniverseInfo(@PathVariable("id") Long id) {
        return universeFileService.loadUniverseInfo(id);
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateUniverseImage(@RequestBody UniverseFileRequestDto universeFileRequestDto) {
        return universeFileService.generateUniverseImage(universeFileRequestDto);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUniverseFile(@RequestBody UniverseFileImageRequestDto universeFileImageRequestDto) {
        return universeFileService.createUniverseFile(universeFileImageRequestDto);
    }
}