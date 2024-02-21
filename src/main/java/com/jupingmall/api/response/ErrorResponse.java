package com.jupingmall.api.response;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

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
@RequiredArgsConstructor
public class ErrorResponse {

    private final HttpStatusCode code;
    private final String message;

    /*Map 말고 개선해보자.*/
    private final Map<String,String> validation = new HashMap<>();

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
