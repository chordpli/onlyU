package com.onlyu.domain.dto.friends;

import com.onlyu.domain.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class FriendResponse {

  private Long memberNo;
  private String nickname;

  public static FriendResponse of(Member member) {
    return FriendResponse.builder()
        .memberNo(member.getMemberNo())
        .nickname(member.getNickname())
        .build();
  }
}
