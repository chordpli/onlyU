package com.onlyu.config;

import com.onlyu.exception.ErrorCode;
import com.onlyu.repository.MemberRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private static final String AUTHORIZATION_HEADER = "Authorization";
	private static final String BEARER_PREFIX = "Bearer ";
	private static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

	private final MemberRepository memberRepository;
	private final JwtUtils jwtUtils;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		final String authHeader = request.getHeader(AUTHORIZATION_HEADER);

		if (isBearerTokenPresent(authHeader)) {
			processBearerToken(request, authHeader);
		}

		filterChain.doFilter(request, response);
	}

	private boolean isBearerTokenPresent(String authHeader) {
		return authHeader != null && authHeader.startsWith(BEARER_PREFIX);
	}

	private void processBearerToken(HttpServletRequest request, String authHeader) {
		String jwtToken = authHeader.substring(BEARER_PREFIX_LENGTH);
		String userEmail = jwtUtils.extractUsername(jwtToken);

		try {
			if (userEmail != null && isUserNotAuthenticated()) {
				UserDetails userDetails = getUserDetails(userEmail);
				authenticateUserIfTokenIsValid(request, jwtToken, userDetails);
			}
		} catch (ExpiredJwtException e) {
			request.setAttribute("exception", ErrorCode.EXPIRED_TOKEN.name());
		} catch (MalformedJwtException e) {
			request.setAttribute("exception", ErrorCode.INVALID_TOKEN.name());
		} catch (JwtException e) {
			request.setAttribute("exception", ErrorCode.NOT_EXIST_TOKEN.name());
		} catch (Exception e) {
			request.setAttribute("exception", ErrorCode.UNKNOWN_ERROR.name());
		}
	}

	private boolean isUserNotAuthenticated() {
		return SecurityContextHolder.getContext().getAuthentication() == null;
	}

	private UserDetails getUserDetails(String userEmail) {
		return memberRepository.findByEmail(userEmail)
			.orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 회원입니다."));
	}

	private void authenticateUserIfTokenIsValid(HttpServletRequest request, String jwtToken, UserDetails userDetails) {
		if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
			authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authToken);
		}
	}
}
