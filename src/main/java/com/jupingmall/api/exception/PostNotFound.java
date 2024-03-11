package com.jupingmall.api.exception;

public class PostNotFound extends RuntimeException { //Unchecked Exception을 상속

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

//    public PostNotFound(Throwable cause) {
//        super(MESSAGE, cause);
//    }
}
