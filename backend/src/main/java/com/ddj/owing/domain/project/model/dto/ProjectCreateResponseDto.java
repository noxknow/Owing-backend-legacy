package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;
import lombok.Builder;

@Builder
public record ProjectCreateResponseDto(
        Long id,
        String title,
        String imageUrl
) {

    public static ProjectCreateResponseDto fromEntity (Project project, String presignedUrl) {

        return ProjectCreateResponseDto.builder()
                .id(project.getId())
                .title(project.getTitle())
                .imageUrl(presignedUrl)
                .build();
    }
}
