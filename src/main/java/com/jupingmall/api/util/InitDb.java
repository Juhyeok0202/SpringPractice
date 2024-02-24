package com.jupingmall.api.util;

import com.jupingmall.api.domain.Post;
import com.jupingmall.api.repository.PostRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final PostRepository postRepository;

        public void dbInit1() {
            Post post1 = Post.builder()
                    .title("title1")
                    .content("content1")
                    .build();

            Post post2 = Post.builder()
                    .title("title2")
                    .content("content2")
                    .build();

            postRepository.saveAll(List.of(post1, post2));
        }
    }

}
