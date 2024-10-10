package com.ddj.owing.domain.member.model.dto;

import com.ddj.owing.domain.member.model.Member;

public record MemberInfoResponseDto(
        Long id,
        String email,
        String name,
        String nickname,
        String profileImage
) {
    public static MemberInfoResponseDto from(Member member) {
        return new MemberInfoResponseDto(
                member.getId(),
                member.getEmail(),
                member.getName(),
                member.getNickname(),
                member.getProfileImage()
        );
    }
}
