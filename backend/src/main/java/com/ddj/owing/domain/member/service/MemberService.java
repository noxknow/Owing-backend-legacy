package com.ddj.owing.domain.member.service;

import com.ddj.owing.domain.member.error.code.MemberErrorCode;
import com.ddj.owing.domain.member.error.exception.MemberException;
import com.ddj.owing.domain.member.model.Member;
import com.ddj.owing.domain.member.model.dto.MemberInfoResponseDto;
import com.ddj.owing.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    public MemberInfoResponseDto findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.of(MemberErrorCode.MEMBER_NOT_FOUND));

        MemberInfoResponseDto memberInfo = MemberInfoResponseDto.from(member);
        return memberInfo;
    }
}
