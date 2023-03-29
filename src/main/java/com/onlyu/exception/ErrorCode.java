package com.onlyu.exception;

import com.onlyu.domain.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum ErrorCode {
  NOT_FOUND(ResultCode.NOT_FOUND, "해당 페이지를 찾을 수 없습니다."),
  MEMBER_NOT_FOUND(ResultCode.NOT_FOUND, "존재하지 않는 사용자입니다."),
  INVALID_PASSWORD(ResultCode.UNAUTHORIZED, "잘못된 비밀번호입니다."),
  INCONSISTENT_INFORMATION(ResultCode.CONFLICT, "일치하지 않는 정보입니다."),
  REJECT_PASSWORD(ResultCode.CONFLICT, "비밀번호는 8~16자입니다."),
  UNKNOWN_ERROR(ResultCode.INVALID_ARGUMENT, "알 수 없는 에러가 발생했습니다."),
  DATABASE_ERROR(ResultCode.INTERNAL_SERVER_ERROR, "DB에러"),
  NOT_EXIST_INQUIRY_TITLE(ResultCode.INVALID_ARGUMENT, "제목이 없습니다."),
  NOT_EXIST_INQUIRY_CONTENT(ResultCode.INVALID_ARGUMENT, "내용이 없습니다."),

  EXPIRED_TOKEN(ResultCode.UNAUTHORIZED, "만료된 토큰입니다."),
  INVALID_TOKEN(ResultCode.UNAUTHORIZED, "잘못된 토큰입니다."),
  INVALID_PERMISSION(ResultCode.UNAUTHORIZED, "사용자가 권한이 없습니다."),
  NOT_EXIST_TOKEN(ResultCode.UNAUTHORIZED, "토큰이 존재하지 않습니다."),
  NOT_ENOUGH_POINTS(ResultCode.INVALID_ARGUMENT, "포인트가 부족합니다."),
  DUPLICATED_MEMBER_EMAIL(ResultCode.CONFLICT, "이미 사용중인 이메일입니다."),
  DUPLICATED_MEMBER_NICKNAME(ResultCode.CONFLICT, "이미 사용중인 닉네임입니다." ),
  DUPLICATED_MEMBER_INFO(ResultCode.CONFLICT, "이미 존재하는 사용자입니다." ),
  ALREADY_REFUSE_REQUEST(ResultCode.CONFLICT, "이미 거절된 요청입니다."),
  ALREADY_ACCEPT_REQUEST(ResultCode.CONFLICT, "이미 승인된 요청입니다.");

  private ResultCode resultCode;
  private String message;
}
