package com.ddj.owing.domain.universe.model.dto;

import com.ddj.owing.domain.universe.model.Universe;

public record UniverseRequestDto(
        String title,
        String description
) {

    public Universe toEntity(String imageUrl) {

        return Universe.builder()
                .title(title)
                .description(description)
                .coverImage(imageUrl)
                .build();
    }
}
