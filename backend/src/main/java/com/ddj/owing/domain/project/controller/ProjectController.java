package com.ddj.owing.domain.project.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddj.owing.domain.project.model.dto.ProjectCreateRequestDto;
import com.ddj.owing.domain.project.model.dto.ProjectCreateResponseDto;
import com.ddj.owing.domain.project.model.dto.ProjectDetailResponseDto;
import com.ddj.owing.domain.project.model.dto.ProjectInfoListResponseDto;
import com.ddj.owing.domain.project.model.dto.ProjectInfoResponseDto;
import com.ddj.owing.domain.project.model.dto.ProjectRequestDto;
import com.ddj.owing.domain.project.model.dto.ProjectUpdateRequestDto;
import com.ddj.owing.domain.project.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/project")
public class ProjectController {

	private final ProjectService projectService;

	@GetMapping
	public ResponseEntity<ProjectInfoListResponseDto> loadProject() {
		return projectService.loadProject();
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<ProjectDetailResponseDto> getProject(@PathVariable Long projectId) {
		ProjectDetailResponseDto projectDetail = projectService.findProject(projectId);
		return ResponseEntity.ok(projectDetail);
	}

	@PostMapping
	public ResponseEntity<ProjectCreateResponseDto> createProject(
		@RequestBody ProjectCreateRequestDto projectCreateRequestDto) {
		return projectService.createProject(projectCreateRequestDto);
	}

	@PostMapping("/image")
	public ResponseEntity<String> generateProjectImage(@RequestBody ProjectRequestDto projectRequestDto) {
		return projectService.generateProjectImage(projectRequestDto);
	}

	@PutMapping("/{projectId}")
	public ResponseEntity<Void> updateProject(@PathVariable Long projectId,
		@RequestBody ProjectUpdateRequestDto projectUpdateRequestDto) {
		projectService.updateProject(projectId, projectUpdateRequestDto);
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/{projectId}")
	public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
		projectService.deleteProject(projectId);
		return ResponseEntity.ok().build();
	}
}
