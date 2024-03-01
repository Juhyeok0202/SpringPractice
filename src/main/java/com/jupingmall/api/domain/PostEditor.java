package com.jupingmall.api.domain;

import lombok.Builder;
import lombok.Getter;

//같은 영역에 위치하도록 domain영역에 클래스 생성
@Getter
public class PostEditor {

    // 수정 가능한 필드들만 기재
    private final String title;
    private final String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
