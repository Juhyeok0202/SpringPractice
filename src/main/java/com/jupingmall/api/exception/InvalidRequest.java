package com.jupingmall.api.exception;

import lombok.Getter;

@Getter
public class InvalidRequest extends GlobalException {

    //PostController의 게시글 등록에서
    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        super.addValidation(fieldName, message);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
