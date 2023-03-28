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
public class SearchResponse {

  private Long memberNo;
  private String email;
  private String nickname;

  public static SearchResponse of(Member member) {
    return SearchResponse.builder()
        .memberNo(member.getMemberNo())
        .email(member.getEmail())
        .nickname(member.getNickname())
        .build();
  }
}
