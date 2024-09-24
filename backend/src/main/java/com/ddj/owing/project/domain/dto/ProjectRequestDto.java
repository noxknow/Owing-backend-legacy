package com.ddj.owing.project.domain.dto;

import com.ddj.owing.project.domain.Project;
import com.ddj.owing.project.domain.enums.Category;
import com.ddj.owing.project.domain.enums.Genre;

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
