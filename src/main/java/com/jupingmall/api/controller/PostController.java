package com.jupingmall.api.controller;

import com.jupingmall.api.domain.Post;
import com.jupingmall.api.request.PostCreate;
import com.jupingmall.api.response.PostResponse;
import com.jupingmall.api.response.Result;
import com.jupingmall.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        // POST MAPPING의 경우 응답 데이터.
        // Case1. 저장한 데이터 Entity -> response로 응답하기
        // Case2. 저장한 데이터의 primar_id -> response로 응답하기(리소스 아이디는 여러 액션으로 필요할 수 있음.)
        //          Client에서는 수신한 id를 post 조회 API를 통해서 글 데이터를 수신받음
        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 content를 잘 관리함
        // Bad Case: 서버에서 반드시 이렇게 할껍니다! fix
        //          -> 서버에서 차라리 유연하게 대응하는게 좋습니다. 코드를 잘 짜야한다!
        //          -> 한 번에 일괄적으로 잘 처리되는 케이스가 없다 -> 잘 관리하는 형태가 중요하다.
//        Long postId = postService.write(request);
//        return Map.of("postId", postId);
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public Result<PostResponse> getList() {
        return new Result(postService.getList());
    }
}
