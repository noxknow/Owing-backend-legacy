package com.ddj.owing.domain.member.model;

import com.ddj.owing.domain.member.model.dto.MemberUpdateRequestDto;
import org.hibernate.annotations.SoftDelete;

import com.ddj.owing.global.entity.BaseTimeEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SoftDelete
public class Member extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String email;
	private String password;
	private String name;
	private String nickname;
	private String profileImage;
	private String oauthId;
	private OauthProvider oauthProvider;

	public void updateMember(MemberUpdateRequestDto memberUpdateRequestDto) {
		this.name = memberUpdateRequestDto.name();
		this.nickname = memberUpdateRequestDto.nickname();
		this.profileImage = memberUpdateRequestDto.profileImage();
	}
}
