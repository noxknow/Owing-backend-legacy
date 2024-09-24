package com.ddj.owing.project.controller;

import com.ddj.owing.project.domain.dto.ProjectRequestDto;
import com.ddj.owing.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping("/project/generate")
    public ResponseEntity<String> generateProjectImage(@RequestBody ProjectRequestDto projectRequestDto) {
        return projectService.generateProjectImage(projectRequestDto);
    }
}
