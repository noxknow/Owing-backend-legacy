package com.ddj.owing.domain.casting.model.dto.casting;

public record CastingInfoUpdateDto(
	String name,
	Long age,
	String gender,
	String role,
	String detail,
	String imageUrl
) {
}
