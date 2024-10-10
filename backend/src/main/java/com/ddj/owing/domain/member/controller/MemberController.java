package com.ddj.owing.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ddj.owing.domain.member.model.dto.MemberInfoResponseDto;
import com.ddj.owing.domain.member.model.dto.MemberUpdateRequestDto;
import com.ddj.owing.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@GetMapping("/{memberId}")
	public ResponseEntity<MemberInfoResponseDto> getMember(@PathVariable Long memberId) {
		MemberInfoResponseDto memberInfo = memberService.findMember(memberId);
		return ResponseEntity.ok(memberInfo);
	}

	@PutMapping("/{memberId}")
	public ResponseEntity<MemberInfoResponseDto> updateMember(@PathVariable Long memberId,
		@RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
		MemberInfoResponseDto memberInfo = memberService.updateMember(memberId, memberUpdateRequestDto);
		return ResponseEntity.ok(memberInfo);
	}

	@DeleteMapping("/{memberId}")
	public ResponseEntity<Void> deleteMember(@PathVariable Long memberId) {
		memberService.deleteMember(memberId);
		return ResponseEntity.ok().build();
	}
}
