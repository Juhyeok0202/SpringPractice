package com.jupingmall.api.repository;

import com.jupingmall.api.domain.Post;
import com.jupingmall.api.domain.QPost;
import com.jupingmall.api.request.PostSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.jupingmall.api.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> getList(PostSearch postSearch) {
        return jpaQueryFactory.selectFrom(post)
                .limit(postSearch.getSize())
                .orderBy(post.id.desc())
                .offset(postSearch.getOffset())
                .fetch();
    }
}
