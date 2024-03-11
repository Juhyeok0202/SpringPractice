package com.jupingmall.api.controller;

import com.jupingmall.api.exception.GlobalException;
import com.jupingmall.api.exception.InvalidRequest;
import com.jupingmall.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    // Spring에서 제공해주는 Exception은 따로 제어
    // 처리해야 될 내용이 Exception 종류마다 조금씩 상이하기 때문
    @ResponseStatus(HttpStatus.BAD_REQUEST) //400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e) {

//            FieldError fieldError = e.getFieldError();
//            String field = fieldError.getField(); // 에러 필드명
//            String message = fieldError.getDefaultMessage(); // 에러 메세지
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }


    // 예외가 많아지면, 최상위 커스텀 예외 클래스를 만들어 제어
    // 직접 예외에게 status와 message를 물어보자
    @ResponseBody
    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorResponse> globalException(GlobalException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response;
    }

//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(InvalidRequest.class)
//    public ErrorResponse invalidRequest(InvalidRequest invalidRequest) {
//        ErrorResponse response = ErrorResponse.builder()
//                .code("400")
//                .message("잘못된 요청입니다.")
//                .build();
//
//        return response;
//    }
}
