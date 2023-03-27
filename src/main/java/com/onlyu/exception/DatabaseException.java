package com.onlyu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class DatabaseException extends RuntimeException {

	private ErrorCode errorCode;
	private int sqlErrorCode;

	public DatabaseException(ErrorCode errorCode, int sqlErrorCode, String message) {
		super(message);
		this.errorCode = errorCode;
		this.sqlErrorCode = sqlErrorCode;
	}
}