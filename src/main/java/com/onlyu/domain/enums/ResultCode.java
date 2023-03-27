package com.onlyu.domain.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResultCode {
	SUCCESS(HttpStatus.OK),
	INVALID_ARGUMENT(HttpStatus.BAD_REQUEST),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
	FORBIDDEN(HttpStatus.FORBIDDEN),
	NOT_FOUND( HttpStatus.NOT_FOUND),
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
	CONFLICT(HttpStatus.CONFLICT);

	private final HttpStatus httpStatus;
}