package com.onlyu.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

	@Value("${jwt.secret}")
	private String secretKey;

	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	private final long accessTokenExpiration = 1000 * 60 * 10; // 10 분
	private final long refreshTokenExpiration = 7 * 24 * 60 * 60 * 1000; // 7 days

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public boolean hasClaim(String token, String claimName) {
		final Claims claims = extractAllClaims(token);
		return claims.get(claimName) != null;
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return generateAccessToken(claims, userDetails);
	}

	public String generateToken(UserDetails userDetails, Map<String, Object> claims) {
		return generateAccessToken(claims, userDetails);
	}

	public String generateAccessToken(Map<String, Object> claims, UserDetails userDetails) {
		return Jwts.builder().setClaims(claims)
			.setSubject(userDetails.getUsername())
			.claim("authorities", userDetails.getAuthorities())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
			.signWith(signatureAlgorithm, secretKey)
			.compact();
	}

	public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
		return Jwts.builder().setClaims(claims)
			.setSubject(userDetails.getUsername())
			.claim("authorities", userDetails.getAuthorities())
			.setIssuedAt(new Date(System.currentTimeMillis()))
			.setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
			.signWith(signatureAlgorithm, secretKey)
			.compact();
	}

	public Boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}