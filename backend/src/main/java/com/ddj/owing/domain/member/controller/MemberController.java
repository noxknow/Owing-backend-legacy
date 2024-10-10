package com.ddj.owing.domain.member.controller;

import com.ddj.owing.domain.member.model.dto.MemberInfoResponseDto;
import com.ddj.owing.domain.member.model.dto.MemberUpdateRequestDto;
import com.ddj.owing.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public ResponseEntity<MemberInfoResponseDto> updateMember(@PathVariable Long id, @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        MemberInfoResponseDto memberInfo = memberService.updateMember(id, memberUpdateRequestDto);
        return ResponseEntity.ok(memberInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.ok().build();
    }
}
