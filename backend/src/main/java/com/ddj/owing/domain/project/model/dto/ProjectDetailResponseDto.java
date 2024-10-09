package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.enums.Category;
import com.ddj.owing.domain.project.model.enums.Genre;

import java.time.LocalDateTime;

public record ProjectDetailResponseDto(
        String title,
        String description,
        Category category,
        Genre genre,
        String coverImage,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static ProjectDetailResponseDto from(Project project) {
        return new ProjectDetailResponseDto(
                project.getTitle(),
                project.getDescription(),
                project.getCategory(),
                project.getGenre(),
                project.getCoverImage(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}
