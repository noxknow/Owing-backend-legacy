package com.ddj.owing.domain.project.service;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.ProjectNode;
import com.ddj.owing.domain.project.model.dto.*;
import com.ddj.owing.domain.project.repository.ProjectNodeRepository;
import com.ddj.owing.domain.project.repository.ProjectRepository;
import com.ddj.owing.domain.project.error.code.ProjectErrorCode;
import com.ddj.owing.domain.project.error.exception.ProjectException;
import com.ddj.owing.domain.story.repository.StoryPlotNodeRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import com.ddj.owing.global.util.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final int MAX_GENRE_COUNT = 5;

    private final ProjectRepository projectRepository;
    private final ProjectNodeRepository projectNodeRepository;
    private final OpenAiUtil openAiUtil;
    private final StoryPlotNodeRepository storyPlotNodeRepository;

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

    @Transactional
    public ProjectCreateResponseDto createProject(ProjectCreateRequestDto projectCreateDto) {
        if (MAX_GENRE_COUNT < projectCreateDto.genres().size()) {
            throw ProjectException.of(ProjectErrorCode.INVALID_GENRE_COUNT);
        }
        Project project = projectCreateDto.toEntity();
        Project savedProject = projectRepository.save(project);

        ProjectNode projectNode = projectCreateDto.toNode(savedProject.getId());
        projectNodeRepository.save(projectNode);
        return new ProjectCreateResponseDto(savedProject.getId());
    }

    @Transactional
    public void updateProject(Long id, ProjectUpdateRequestDto projectUpdateRequestDto) {
        if (MAX_GENRE_COUNT < projectUpdateRequestDto.genres().size()) {
            throw ProjectException.of(ProjectErrorCode.INVALID_GENRE_COUNT);
        }
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NOT_FOUND));
        project.update(projectUpdateRequestDto);

        ProjectNode projectNode = projectNodeRepository.findById(id)
                .orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NODE_NOT_FOUND));
        projectNode.updateTitle(projectUpdateRequestDto.title());
        projectNodeRepository.save(projectNode);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> ProjectException.of(ProjectErrorCode.PROJECT_NOT_FOUND));
        projectRepository.delete(project);

        projectNodeRepository.findById(id).ifPresentOrElse(
                (node) -> {
                    node.delete();
                    projectNodeRepository.save(node);
                },
                () -> log.warn("Project 데이터 불일치 발생. entity id:{}", project.getId())
        );
    }
}
