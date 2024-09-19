package com.exercise.exception;

import lombok.*;
import lombok.Getter;
import org.antlr.v4.runtime.misc.NotNull;


@Getter
public class SalaryException extends RuntimeException {

    private ErrorCode errorCode;

    public SalaryException(String message) {
        super(message);
    }

    public SalaryException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
