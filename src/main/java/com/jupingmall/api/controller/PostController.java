package com.jupingmall.api.controller;

import com.jupingmall.api.exception.InvalidRequest;
import com.jupingmall.api.request.PostCreate;
import com.jupingmall.api.request.PostEdit;
import com.jupingmall.api.request.PostSearch;
import com.jupingmall.api.response.PostResponse;
import com.jupingmall.api.response.Result;
import com.jupingmall.api.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
        request.validate();
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    // 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
    // 글이 -> 100,000,000 -> DB글 모두 조회하는경우 -> DB 뻗을 수 있다.
    // DB -> 애플리케이션 서버로 전달하는 시간, 트래픽비용 등이 많이 발생할 수 있다.
    @GetMapping("/posts")
    public Result<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return new Result(postService.getList(postSearch));
    }

    @PatchMapping("/posts/{postId}") //수정에 관한 것은 응답으로 안주는 편이지만, 클라이언트 측에서 필요하다면 변경된 엔티티를 주기도 함
    public PostResponse edit(@PathVariable Long postId, @ModelAttribute PostEdit request) {
        return postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

    /**
     * TODO : 인증 권한 작업, 예외처리
     * 현재 상태에서는 해당 API의 URL을 알고 있는 사람 누구나 게시글을 CUD할 수 있음
     * 인증 시스템 도입 필요.
     * 예외 처리를 이쁘게 해놓지 못함.
     */
}
