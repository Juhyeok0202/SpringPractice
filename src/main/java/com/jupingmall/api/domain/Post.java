package com.jupingmall.api.domain;

import com.jupingmall.api.request.PostCreate;
import com.jupingmall.api.request.PostEdit;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    private Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static Post createPost(PostCreate postCreate) {
        return Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();
    }

    public PostEditor.PostEditorBuilder toEditor() {
        // Post Entity가 가진 값으로 빌더를 만든다. -> data fix를 시키지 X
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }

//    public void change(PostEdit postEdit) {
//        title = postEdit.getTitle() != null ? postEdit.getTitle() : this.title;
//        content = postEdit.getContent() != null ? postEdit.getContent() : this.content;
//    }

//    public String getTitle() {
//        // 서비스의 정책을 절대 넣지 말자.(서비스가 엔티티의 정책을 같이 공유하게됨.)
//        return this.title.substring(0, 10);
//    }
}
