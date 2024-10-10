package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;

import java.time.LocalDateTime;

public record ProjectInfoResponseDto(
        String title,
        LocalDateTime updatedAt,
        String coverImage
) {

    public static ProjectInfoResponseDto from(Project project) {
        return new ProjectInfoResponseDto(
                project.getTitle(),
                project.getUpdatedAt(),
                project.getImageUrl()
        );
    }
}
