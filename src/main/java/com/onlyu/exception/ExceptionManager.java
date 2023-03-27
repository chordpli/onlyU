package com.onlyu.exception;

import com.onlyu.domain.Response;
import com.onlyu.domain.enums.ResultCode;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

  @ExceptionHandler(OnlyUAppException.class)
  public ResponseEntity<?> SlogAppExceptionHandler(OnlyUAppException e) {
    Map<String, Object> result = new HashMap<>();
    result.put("errorCode", e.getErrorCode());
    result.put("message", e.getMessage());
    return ResponseEntity.status(e.getErrorCode().getResultCode().getHttpStatus())
        .body(Response.error(e.getErrorCode().getResultCode(), result));
  }

  @ExceptionHandler(DatabaseException.class)
  public ResponseEntity<?> databaseExceptionHandler(DatabaseException e) {
    Map<String, Object> result = new HashMap<>();
    result.put("errorCode", e.getErrorCode());
    result.put("sqlErrorCode", e.getSqlErrorCode());
    result.put("message", e.getMessage());
    return ResponseEntity.status(e.getErrorCode().getResultCode().getHttpStatus())
        .body(Response.error(ResultCode.valueOf(e.getErrorCode().getResultCode().getHttpStatus().name()), result));
  }
}