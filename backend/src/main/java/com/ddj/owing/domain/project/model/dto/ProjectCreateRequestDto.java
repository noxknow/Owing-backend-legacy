package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.enums.Category;
import com.ddj.owing.domain.project.model.enums.Genre;

public record ProjectCreateRequestDto(
        String title,
        String description,
        Category category,
        Genre genre,
        String coverImage
) {

    public Project toEntity() {
        return Project.builder()
                .title(title)
                .description(description)
                .category(category)
                .genre(genre)
                .coverImage(coverImage)
                .build();
    }
}
