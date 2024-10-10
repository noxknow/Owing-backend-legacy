package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectInfoListResponseDto(
        List<ProjectInfoResponseDto> projects
) {

    public static ProjectInfoListResponseDto fromEntity(List<Project> projects) {

        List<ProjectInfoResponseDto> projectInfoListResponseDto = projects.stream()
                .map(ProjectInfoResponseDto::from)
                .toList();

        return ProjectInfoListResponseDto.builder()
                .projects(projectInfoListResponseDto)
                .build();
    }
}
