package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.enums.Category;
import com.ddj.owing.domain.project.model.enums.Genre;

import java.util.Set;

public record ProjectCreateRequestDto(
        String title,
        String description,
        Category category,
        Set<Genre> genres,
        String coverImage
) {

    public Project toEntity() {
        return Project.builder()
                .title(title)
                .description(description)
                .category(category)
                .genres(genres)
                .coverImage(coverImage)
                .build();
    }
}
