package com.jupingmall.api.request;


import lombok.Builder;
import lombok.Data;

import static java.lang.Math.max;

@Data
@Builder // @Builder.Default 가 생성자가 아닌 Class레벨에서 적용이 됨.
public class PostSearch {
    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private int page = 1;

    @Builder.Default
    private int size = 10; // per page

    public long getOffset() {
        return (long) (max(1,page) - 1) * max(size, MAX_SIZE);
//        return (long)(page - 1) * size;
    }

//    @Builder
//    public PostSearch(Integer page, Integer size) {
//        this.page = page;
//        this.size = size;
//    }
}
