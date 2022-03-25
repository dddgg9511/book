package com.example.book.domain;

import javafx.geometry.Pos;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Comments extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @JoinColumn(name = "post_id")
    private Long postId;

    @Column(name = "comment_content")
    private String content;

    private String email;

    @Builder
    public Comments(Long id, Long postId, String content, String email) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.email = email;
    }

}
