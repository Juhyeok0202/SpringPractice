package com.jupingmall.api.service;

import com.jupingmall.api.domain.Post;
import com.jupingmall.api.domain.PostEditor;
import com.jupingmall.api.repository.PostRepository;
import com.jupingmall.api.request.PostCreate;
import com.jupingmall.api.request.PostEdit;
import com.jupingmall.api.request.PostSearch;
import com.jupingmall.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.jupingmall.api.domain.Post.createPost;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
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

        /** 답 (정답은 없지만, 아래 방법도 있다.)
         * Controller ->  WebPostService : response를 위함 -> Repository
         *                PostService : 다른 서비스와 통신하기 위함
         */

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        // web -> page 1 -> (내부적으로) 0 변경 -> 수동으로 1로 해봤자 테스트 불가능(웹 요청을 통한 테스트 필요)
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());

    }

    @Transactional
    public PostResponse edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

//        //setting (Dirty Checking)
//        post.change(postEdit);


        /*[Post에 보조메서드 안만들고, Editor를 따로 만드는 이유]*/
        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);
        return new PostResponse(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));

        // -> 존재하는 경우
        postRepository.delete(post);
    }
}
