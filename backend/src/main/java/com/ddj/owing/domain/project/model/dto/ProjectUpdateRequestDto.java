package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.enums.Category;
import com.ddj.owing.domain.project.model.enums.Genre;

public record ProjectUpdateRequestDto(
        String title,
        String description,
        Category category,
        Genre genre,
        String coverImage
) {
}
