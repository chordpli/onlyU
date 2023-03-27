package com.onlyu.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class OnlyUAppException extends RuntimeException {

    private ErrorCode errorCode;

    public OnlyUAppException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
