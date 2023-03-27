package com.onlyu.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlyu.domain.Response;
import com.onlyu.domain.enums.ResultCode;
import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");
        ErrorCode errorCode = null;

        log.debug("log: exception: {} ", exception);

        for (ErrorCode e : ErrorCode.values()) {
            if (e.name().equals(exception)) {
                errorCode = e;
                break;
            }
        }

        if (errorCode == null) {
            errorCode = ErrorCode.UNKNOWN_ERROR;
            log.info("알 수 없는 에러가 발생하였습니다.");
        } else {
            log.info("{}: {}", errorCode.name(), errorCode.getMessage());
        }

        setResponse(response, errorCode);
    }

    private void setResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {

        ResultCode resultCode = errorCode.getResultCode();
        Response<Map<String, Object>> errorResponse = Response.error(resultCode, Map.of("errorCode", errorCode.name(), "message", errorCode.getMessage()));
        ResponseEntity<Object> responseEntity = errorResponse.toResponseEntity();

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(responseEntity.getStatusCodeValue());

        response.getWriter().println(objectMapper.writeValueAsString(responseEntity.getBody()));
    }
}