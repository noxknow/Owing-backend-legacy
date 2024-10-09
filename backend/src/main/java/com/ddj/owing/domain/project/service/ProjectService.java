package com.ddj.owing.domain.project.service;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.dto.*;
import com.ddj.owing.domain.project.repository.ProjectRepository;
import com.ddj.owing.domain.project.error.code.ProjectErrorCode;
import com.ddj.owing.domain.project.error.exception.ProjectException;
import com.ddj.owing.global.util.OpenAiUtil;
import com.ddj.owing.global.util.Parser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final OpenAiUtil openAiUtil;

    public ResponseEntity<List<ProjectInfoResponseDto>> loadProject() {

        List<Project> projects = projectRepository.findTop3ByOrderByUpdatedAtDesc();

        if (projects.isEmpty()) {
            throw ProjectException.of(ProjectErrorCode.PROJECT_NOT_FOUND);
        }

        List<ProjectInfoResponseDto> projectInfoResponseDto = projects.stream()
                .map(ProjectInfoResponseDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(projectInfoResponseDto);
    }

    @Transactional
    public ResponseEntity<String> generateProjectImage(ProjectRequestDto projectRequestDto) {

        String prompt = openAiUtil.createPrompt(projectRequestDto);
        String jsonString = openAiUtil.createImage(prompt);
        String imageUrl = Parser.extractUrl(jsonString);
        Project project = projectRequestDto.toEntity(imageUrl);

        projectRepository.save(project);

        return ResponseEntity.ok(imageUrl);
    }

    public ProjectDetailResponseDto findProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NOT_FOUND));
        return ProjectDetailResponseDto.from(project);
    }
}
