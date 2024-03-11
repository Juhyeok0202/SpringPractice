package com.jupingmall.api.request;

import com.jupingmall.api.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void validate() {
        if (title.contains("바보")) {
            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
        }
    }

    /*🌟빌더의 장점*/
    // - 가독성에 좋다.(값 생성에 대한 유연함)
        //생성자의 파라미터가 많아졌을 때, 값을 넘기기 쉬워짐.
        // new Create(... ,.... ,... ,.., .., ... ,....... ,..)
    // - 필요한 값만 받을 수 있다. // -> (오버로딩 가능한 조건도 숙지)
        // 생성자 Overloading 필요 X
    // - 객체의 불변성(제일 중요함)
}
