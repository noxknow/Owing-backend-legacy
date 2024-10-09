package com.ddj.owing.domain.project.model.dto;

import com.ddj.owing.domain.project.model.enums.Category;
import com.ddj.owing.domain.project.model.enums.Genre;

import java.util.Set;

public record ProjectUpdateRequestDto(
        String title,
        String description,
        Category category,
        Set<Genre> genres,
        String coverImage
) {
}
