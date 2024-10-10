package com.ddj.owing.domain.member.model.dto;

import com.ddj.owing.domain.member.model.Member;

public record MemberUpdateRequestDto(
        String name,
        String nickname,
        String profileImage
) {
}
