package com.ddj.owing.domain.universe.controller;

import com.ddj.owing.domain.universe.model.UniverseFolder;
import com.ddj.owing.domain.universe.model.dto.UniverseFolderCreateDto;
import com.ddj.owing.domain.universe.service.UniverseFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/universe/folder")
public class UniverseFolderController {

    private final UniverseFolderService universeFolderService;

    @PostMapping("/create")
    public ResponseEntity<String> createFolder(@RequestBody UniverseFolderCreateDto universeFolderCreateDto) {
        return universeFolderService.createFolder(universeFolderCreateDto);
    }
}
