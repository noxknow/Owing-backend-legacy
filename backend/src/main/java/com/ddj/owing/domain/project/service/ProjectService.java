package com.ddj.owing.domain.project.service;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.dto.ProjectRequestDto;
import com.ddj.owing.domain.project.repository.ProjectRepository;
import com.ddj.owing.global.util.OpenAiUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final OpenAiUtil openAiUtil;

    public ResponseEntity<String> generateProjectImage(ProjectRequestDto projectRequestDto) {

        String prompt = openAiUtil.createPrompt(projectRequestDto);
        String imageUrl = openAiUtil.createImage(prompt);
        Project project = projectRequestDto.toEntity(imageUrl);

        projectRepository.save(project);

        return ResponseEntity.ok().body(imageUrl);
    }
}
