package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.Project;
import com.ddj.owing.domain.project.model.ProjectNode;
import com.ddj.owing.domain.project.model.enums.Category;
import com.ddj.owing.domain.project.model.enums.Genre;

import java.util.Set;

public record ProjectCreateRequestDto(
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

    public ProjectNode toNode(Long id) {
        return new ProjectNode(id, title);
    }
}
