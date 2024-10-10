package com.ddj.owing.domain.member.service;

import com.ddj.owing.domain.member.error.code.MemberErrorCode;
import com.ddj.owing.domain.member.error.exception.MemberException;
import com.ddj.owing.domain.member.model.Member;
import com.ddj.owing.domain.member.model.dto.MemberInfoResponseDto;
import com.ddj.owing.domain.member.model.dto.MemberUpdateRequestDto;
import com.ddj.owing.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberInfoResponseDto findMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.of(MemberErrorCode.MEMBER_NOT_FOUND));

        MemberInfoResponseDto memberInfo = MemberInfoResponseDto.from(member);
        return memberInfo;
    }

    @Transactional
    public MemberInfoResponseDto updateMember(Long id, MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> MemberException.of(MemberErrorCode.MEMBER_NOT_FOUND));
        member.updateMember(memberUpdateRequestDto);

        MemberInfoResponseDto updatedMemberInfo = MemberInfoResponseDto.from(member);
        return updatedMemberInfo;
    }

    @Transactional
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
