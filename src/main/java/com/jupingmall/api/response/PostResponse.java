package com.jupingmall.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Subselect;
import org.hibernate.validator.constraints.Length;

/**
 * 서비스 정책에 맞는 응답 클래스(분리)
 */
@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10)); // 사실 클라이언트에서 하는게 맞지만, 굳이 한다면
        this.content = content;
    }
}
