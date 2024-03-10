package com.jupingmall.api.service;

import com.jupingmall.api.domain.Post;
import com.jupingmall.api.repository.PostRepository;
import com.jupingmall.api.request.PostCreate;
import com.jupingmall.api.request.PostEdit;
import com.jupingmall.api.request.PostSearch;
import com.jupingmall.api.response.PostResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.Sort.Direction.DESC;


@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    public void test1() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());

    }

    @Test
    @DisplayName("글 1개 조회")
    public void test2() throws Exception {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        postRepository.save(post);

        //when
        PostResponse response = postService.get(post.getId());

        //then
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("foo", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 1페이지 조회")
    public void test3() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("테스트 제목 " + i)
                        .content("테스트 내용 " + i)
                        .build())
                .collect(toList());
        postRepository.saveAll(requestPosts);

        // sql -> select, limit, offset
//        Pageable pageable = PageRequest.of(0, 5, Sort.by(DESC, "id"));
        PostSearch postSearch = PostSearch.builder()
                .page(1)
//                .size(10) // 잘 변할 일이 없다. (화면 정책이 바뀌지 않는 이상.)
                .build();
        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10L, posts.size());
        assertEquals("테스트 제목 30", posts.get(0).getTitle());
        assertEquals("테스트 내용 26", posts.get(4).getContent());

    }

    @Test
    @DisplayName("글 제목 수정")
    public void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("원본 제목")
                .content("원본 내용")
                .build();
        postRepository.save(post);

        PostEdit request = PostEdit.builder()
                .title("수정 제목")
                .content(null)
                .build();

        //when
        postService.edit(post.getId(), request);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않스비다. id = " + post.getId()));

        Assertions.assertEquals("수정 제목", changedPost.getTitle());
        Assertions.assertEquals("원본 내용", changedPost.getContent());

    }

    @Test
    @DisplayName("글 내용 수정")
    public void test5() throws Exception {
        //given
        Post post = Post.builder()
                .title("원본 제목")
                .content("원본 내용")
                .build();
        postRepository.save(post);

        PostEdit request = PostEdit.builder()
//                .title("수정 제목")
                .content("수정 내용")
                .build();

        //when
        postService.edit(post.getId(), request);

        //then
        Post changedPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않스비다. id = " + post.getId()));

        Assertions.assertEquals("원본 제목", changedPost.getTitle());
        Assertions.assertEquals("수정 내용", changedPost.getContent());

    }
}