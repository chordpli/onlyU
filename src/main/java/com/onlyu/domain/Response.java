package com.onlyu.domain;

import com.onlyu.domain.enums.ResultCode;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class Response<T> {
	private final ResultCode resultCode;
	private final T data;

	private Response(ResultCode resultCode, T data) {
		this.resultCode = resultCode;
		this.data = data;
	}

	public static <T> Response<T> success(T data) {
		return new Response<>(ResultCode.SUCCESS, data);
	}

	public static <T> Response<T> error(ResultCode resultCode) {
		return new Response<>(resultCode, null);
	}

	public static <T> Response<T> error(ResultCode resultCode, T data) {
		return new Response<>(resultCode, data);
	}

	public ResponseEntity<Object> toResponseEntity() {
		Map<String, Object> responseMap = new HashMap<>();
		responseMap.put("code", resultCode.getHttpStatus().value());
		responseMap.put("message", resultCode.getHttpStatus().getReasonPhrase());
		responseMap.put("data", data);

		return ResponseEntity.status(resultCode.getHttpStatus()).body(responseMap);
	}
}