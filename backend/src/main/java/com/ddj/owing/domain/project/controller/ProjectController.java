package com.ddj.owing.domain.project.controller;

import com.ddj.owing.domain.project.model.dto.ProjectInfoResponseDto;
import com.ddj.owing.domain.project.model.dto.ProjectRequestDto;
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
}
