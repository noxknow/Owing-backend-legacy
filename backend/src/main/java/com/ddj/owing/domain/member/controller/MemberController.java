package com.ddj.owing.domain.member.controller;

import com.ddj.owing.domain.member.model.dto.MemberInfoResponseDto;
import com.ddj.owing.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public ResponseEntity<MemberInfoResponseDto> getMember(@PathVariable Long id) {
        MemberInfoResponseDto memberInfo = memberService.findMember(id);
        return ResponseEntity.ok(memberInfo);
    }

}
