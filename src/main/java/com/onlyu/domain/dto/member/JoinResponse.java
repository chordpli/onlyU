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
public class JoinResponse {

  private Long memberNo;
  private String nickname;


  public static JoinResponse of(Member member) {
    return JoinResponse.builder()
        .memberNo(member.getMemberNo())
        .nickname(member.getNickname())
        .build();
  }
}
