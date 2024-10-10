package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.enums.Genre;
import com.ddj.owing.domain.project.model.enums.Category;

import java.util.Set;

public record ProjectRequestDto(
        String title,
        String description,
        Category category,
        Set<Genre> genres
) {

    public Project toEntity(String imageUrl) {

        return Project.builder()
                .title(title)
                .description(description)
                .category(category)
                .genres(genres)
                .imageUrl(imageUrl)
                .build();
    }
}
