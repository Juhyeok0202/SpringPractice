package com.jupingmall.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;
}
