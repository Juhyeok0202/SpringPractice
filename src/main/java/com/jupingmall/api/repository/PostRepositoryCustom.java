package com.jupingmall.api.repository;

import com.jupingmall.api.domain.Post;
import com.jupingmall.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
