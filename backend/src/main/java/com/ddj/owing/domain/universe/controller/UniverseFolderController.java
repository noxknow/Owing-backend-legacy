package com.ddj.owing.domain.universe.controller;

import com.ddj.owing.domain.universe.model.UniverseFolder;
import com.ddj.owing.domain.universe.model.dto.UniverseFolderCreateDto;
import com.ddj.owing.domain.universe.service.UniverseFolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/universe/folder")
public class UniverseFolderController {

    private final UniverseFolderService universeFolderService;

    @PostMapping("/create")
    public ResponseEntity<Void> createFolder(@RequestBody UniverseFolderCreateDto universeFolderCreateDto) {
        return universeFolderService.createFolder(universeFolderCreateDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<UniverseFolder>> getAllFolders() {
        return universeFolderService.getAllFolders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UniverseFolder> getFolderById(@PathVariable("id") Long id) {
        return universeFolderService.getFolderById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteFolder(@PathVariable("id") Long id) {
        return universeFolderService.deleteFolder(id);
    }
}
