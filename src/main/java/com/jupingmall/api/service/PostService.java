package com.jupingmall.api.service;

import com.jupingmall.api.domain.Post;
import com.jupingmall.api.repository.PostRepository;
import com.jupingmall.api.request.PostCreate;
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
}
