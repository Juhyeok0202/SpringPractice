package com.jupingmall.api.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 *  {
 *      "code" : "400",
 *      "message" : 잘못된 요청입니다.",
 *      "validation" : {
 *          "title" : "값을 입력해주세요."
 *  *      }
 *  }
 */
@Getter
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY) // 비어있지 않은 데이터만 내려감(비어 있는 대로 하나의 정보로 보는 편이 선호됨)
public class ErrorResponse {

    private final String code;
    private final String message;

    /*Map 말고 개선해보자.*/
    private final Map<String,String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
