package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ProjectInfoResponseDto(
        Long id,
        String title,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        String imageUrl
) {

    public static ProjectInfoResponseDto from(Project project) {

        return ProjectInfoResponseDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .imageUrl(project.getImageUrl())
                .build();
    }
}
