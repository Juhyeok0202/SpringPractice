package com.jupingmall.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class GlobalException extends RuntimeException{
    // abstract class로 만든 이유 ->

    private final Map<String, String> validation = new HashMap<>();

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message) {
        this.validation.put(fieldName, message);
    }
}
