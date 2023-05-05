package com.onlyu.domain.dto.member;

import com.onlyu.domain.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class LoginResponse {

	private String jwt;
	private Long memberNo;
	private String nickname;

	public static LoginResponse of(Member member, String jwt) {
		return LoginResponse.builder()
			.jwt(jwt)
			.memberNo(member.getMemberNo())
			.nickname(member.getNickname())
			.build();
	}
}
