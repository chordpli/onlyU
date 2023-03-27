package com.onlyu.domain.dto.member;

import com.onlyu.domain.entity.Member;
import com.onlyu.domain.entity.Member.MemberStatus;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.utils.PasswordValidator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class JoinRequest {

  private String email;
  private String password;
  private String nickname;

  public static Member toEntity(JoinRequest request, PasswordEncoder passwordEncoder) {
    if (!PasswordValidator.isValid(request.getPassword())) {
      throw new OnlyUAppException(ErrorCode.INVALID_PASSWORD, "비밀번호는 최소 8자리 이상이며, 대문자, 소문자, 숫자, 특수문자를 모두 포함해야 합니다.");
    }

    return Member.builder()
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .nickname(request.getNickname())
        .memberStatus(MemberStatus.USER)
        .build();
  }
}
