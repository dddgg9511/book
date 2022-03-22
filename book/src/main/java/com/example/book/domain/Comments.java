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

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Posts posts;

    @Column(name = "comment_content")
    private String content;

    private String email;

    @Builder
    public Comments(Long id, Posts posts, String content, String email) {
        this.id = id;
        this.posts = posts;
        this.content = content;
        this.email = email;
    }

}
