package com.jupingmall.api.domain;

import com.jupingmall.api.request.PostCreate;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String title;

    @Lob // DB에서는 Long Text 형태로 생성되도록 함
    private String content;

    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Post createPost(PostCreate postCreate) {
        return new Post(postCreate.getTitle(), postCreate.getContent());
    }
}
