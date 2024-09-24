package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.enums.Genre;
import com.ddj.owing.domain.project.model.enums.Category;

public record ProjectRequestDto(
        String title,
        String description,
        Category category,
        Genre genre
) {

    public Project toEntity(String imageUrl) {

        return Project.builder()
                .title(title)
                .description(description)
                .category(category)
                .genre(genre)
                .coverImage(imageUrl)
                .build();
    }
}
