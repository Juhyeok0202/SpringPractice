package com.jupingmall.api.service;

import com.jupingmall.api.domain.Post;
import com.jupingmall.api.repository.PostRepository;
import com.jupingmall.api.request.PostCreate;
import com.jupingmall.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.jupingmall.api.domain.Post.createPost;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = createPost(postCreate);
        postRepository.save(post);

//        return post.getId();
    }

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        // 응답 클래스 분리하세요 (서비스 정책에 맞는)
        // entity to response 작업을 여기서 하는게 맞을까?
        PostResponse response = PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        /** 답 (정답은 없지만, 아래 방법도 있다.)
         * Controller ->  WebPostService : response를 위함 -> Repository
         *                PostService : 다른 서비스와 통신하기 위함
         */

        return response;
    }
}
