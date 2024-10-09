package com.ddj.owing.domain.project.controller;

import com.ddj.owing.domain.project.model.dto.*;
import com.ddj.owing.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping("/load")
    public ResponseEntity<List<ProjectInfoResponseDto>> loadProject() {
        return projectService.loadProject();
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generateProjectImage(@RequestBody ProjectRequestDto projectRequestDto) {
        return projectService.generateProjectImage(projectRequestDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDetailResponseDto> getProject(@PathVariable Long id) {
        ProjectDetailResponseDto projectDetail = projectService.findProject(id);
        return ResponseEntity.ok(projectDetail);
    }

    @PostMapping
    public ResponseEntity<ProjectCreateResponseDto> createProject(@RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
        ProjectCreateResponseDto projectCreateResponse = projectService.createProject(projectCreateRequestDto);
        return ResponseEntity.ok(projectCreateResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProject(@PathVariable Long id, @RequestBody ProjectUpdateRequestDto projectUpdateRequestDto) {
        projectService.updateProject(id, projectUpdateRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.ok().build();
    }
}
