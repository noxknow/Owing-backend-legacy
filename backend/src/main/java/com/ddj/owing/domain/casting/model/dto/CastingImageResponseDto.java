package com.ddj.owing.domain.casting.model.dto;

import com.ddj.owing.domain.casting.model.Casting;
import com.ddj.owing.domain.casting.model.Position;
import lombok.Builder;

@Builder
public record CastingImageResponseDto(
        Long id,
        String name,
        Long age,
        String gender,
        String role,
        String detail,
        String imageUrl,
        String presignedUrl,
        Position position
) {

    public static CastingImageResponseDto fromEntity(Casting casting, String presignedUrl) {

        return CastingImageResponseDto.builder()
                .id(casting.getId())
                .name(casting.getName())
                .age(casting.getAge())
                .gender(casting.getGender())
                .role(casting.getRole())
                .detail(casting.getDetail())
                .imageUrl(casting.getImageUrl())
                .presignedUrl(presignedUrl)
                .position(new Position(casting.getCoordX(), casting.getCoordY()))
                .build();
    }
}
