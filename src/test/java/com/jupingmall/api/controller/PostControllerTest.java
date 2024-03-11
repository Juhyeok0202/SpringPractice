package com.jupingmall.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jupingmall.api.domain.Post;
import com.jupingmall.api.repository.PostRepository;
import com.jupingmall.api.request.PostCreate;
import com.jupingmall.api.request.PostEdit;
import com.jupingmall.api.request.PostSearch;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World를 출력한다.")
    void test() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .title("제목입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);//Json 형태로 가공(실무에서 많이 쓰임)

        System.out.println(json); //{"title":"제목입니다.","content":"내용입니다."}

        // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print()); //HTTP 요청에 대한 Summary를 알려줌
    }

    @Test
    @DisplayName("/posts 요청시 title값은 필수다.")
    void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

          // expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                /*Test가 하나의 에러메세지에 특정되어 개선해야할 여지가 있다.*/
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print()); //HTTP 요청에 대한 Summary를 알려줌
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    public void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .title("제목입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);


        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        //then
        assertEquals(1L,postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    public void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();

        postRepository.save(post);

        // 클라이언트 요구사항
            // json응답에서 title값 길이를 최대 10글자로 해주세요.

        // expected (when + then)
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    public void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("테스트 제목 " + i)
                        .content("테스트 내용 " + i)
                        .build())
                .collect(toList());

        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=1&size=10") ///posts?page=1&sort=id,desc&size=5
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(10)))
                .andExpect(jsonPath("$.data[0].id").value(30))
                .andExpect(jsonPath("$.data[0].title").value("테스트 제목 30"))
                .andExpect(jsonPath("$.data[0].content").value("테스트 내용 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    public void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> Post.builder()
                        .title("테스트 제목 " + i)
                        .content("테스트 내용 " + i)
                        .build())
                .collect(toList());

        postRepository.saveAll(requestPosts);

        //expected
        mockMvc.perform(get("/posts?page=0&size=10") ///posts?page=1&sort=id,desc&size=5
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()", is(10)))
                .andExpect(jsonPath("$.data[0].id").value(30))
                .andExpect(jsonPath("$.data[0].title").value("테스트 제목 30"))
                .andExpect(jsonPath("$.data[0].content").value("테스트 내용 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                .title("원본 제목")
                .content("원본 내용")
                .build();

        postRepository.save(post);

        PostEdit request = PostEdit.builder()
                .title("수정 제목")
//                .content("원본 내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/posts/{postId}",post.getId())
                .contentType(APPLICATION_JSON)
                .content(json)
        )
                .andExpect(status().isOk())
                .andDo(print());

        //
//        Post changedPost = postRepository.findById(post.getId()).get();
//        assertEquals("수정 제목", changedPost.getTitle());
//        assertEquals("원본 내용", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    public void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertEquals(0, postRepository.count());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    public void test9() throws Exception {
        //expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    public void test10() throws Exception {
        //when
        PostEdit request = PostEdit.builder()
                .title("수정 제목")
                .content("수정 내용")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    public void test11() throws Exception {
        //given
        PostCreate post = PostCreate.builder()
                .title("나는 정말 바보!")
                .content("내용")
                .build();
        String json = objectMapper.writeValueAsString(post);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());

    }
}