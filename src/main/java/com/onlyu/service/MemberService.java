package com.onlyu.service;

import com.onlyu.config.JwtUtils;
import com.onlyu.domain.dto.member.JoinRequest;
import com.onlyu.domain.dto.member.JoinResponse;
import com.onlyu.domain.dto.member.LoginRequest;
import com.onlyu.domain.dto.member.LoginResponse;
import com.onlyu.domain.dto.member.SearchResponse;
import com.onlyu.domain.entity.Member;
import com.onlyu.exception.ErrorCode;
import com.onlyu.exception.OnlyUAppException;
import com.onlyu.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

	private final MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder;
	private final JwtUtils jwtUtils;
	private final AuthenticationManager authenticationManager;

	@Transactional
	public JoinResponse join(JoinRequest request) {
		// 중복된 이메일이나 닉네임이 이미 존재하는 경우 예외를 발생시킴
		if (memberRepository.existsByEmail(request.getEmail())) {
			throw new OnlyUAppException(ErrorCode.DUPLICATED_MEMBER_EMAIL,
				ErrorCode.DUPLICATED_MEMBER_EMAIL.getMessage());
		}
		if (memberRepository.existsByNickname(request.getNickname())) {
			throw new OnlyUAppException(ErrorCode.DUPLICATED_MEMBER_NICKNAME,
				ErrorCode.DUPLICATED_MEMBER_NICKNAME.getMessage());
		}

		log.info("service request ={}", request.getNickname());
		Member member = memberRepository.save(JoinRequest.toEntity(request, passwordEncoder));
		log.info("service member ={}", member.getNickname());
		return JoinResponse.of(member);
	}

	@Transactional
	public LoginResponse authentication(LoginRequest request) {
		final Member member = memberRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new OnlyUAppException(ErrorCode.MEMBER_NOT_FOUND, "존재하지 않는 회원입니다."));

		log.info("login member = {}", member.getNickname());
		if (passwordEncoder.matches(request.getPassword(), member.getPassword())) {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
			);

			log.info("인증 성공: {}", request.getEmail());
			String jwt = jwtUtils.generateToken(member);
			log.info("jwt ={}", jwt);
			return LoginResponse.of(member, jwt);
		}

		throw new OnlyUAppException(ErrorCode.INCONSISTENT_INFORMATION,
			ErrorCode.INCONSISTENT_INFORMATION.getMessage());
	}

	@Transactional
	public Page<SearchResponse> findMember(String keyword, Pageable pageable) {
		return memberRepository.findAllByEmailIsContainingIgnoreCase(keyword, pageable)
			.map(SearchResponse::of);
	}
}
