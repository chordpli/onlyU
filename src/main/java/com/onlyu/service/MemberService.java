package com.onlyu.service;

import com.onlyu.domain.dto.member.JoinRequest;
import com.onlyu.domain.dto.member.JoinResponse;
import com.onlyu.domain.entity.Member;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  private final PasswordEncoder passwordEncoder;

  public JoinResponse join(JoinRequest request) {
    // 중복된 이메일이나 닉네임이 이미 존재하는 경우 예외를 발생시킴
    if (memberRepository.existsByMemberEmail(request.getEmail())) {
      throw new OnlyUAppException(ErrorCode.DUPLICATED_MEMBER_EMAIL, ErrorCode.DUPLICATED_MEMBER_EMAIL.getMessage());
    }
    if (memberRepository.existsByMemberNickname(request.getNickname())) {
      throw new OnlyUAppException(ErrorCode.DUPLICATED_MEMBER_NICKNAME, ErrorCode.DUPLICATED_MEMBER_NICKNAME.getMessage());
    }

    Member member = memberRepository.save(JoinRequest.toEntity(request, passwordEncoder));

    return JoinResponse.of(member);
  }
}
