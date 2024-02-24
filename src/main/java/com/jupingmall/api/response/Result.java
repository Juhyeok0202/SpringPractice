package com.jupingmall.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class Result<T>{

    private T data;
}
