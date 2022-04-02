package com.example.book.domain;

import javax.persistence.*;

@Entity
public class PostUpdateLog extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_update_id")
    private Long id;

    private Long userId;

    private Long postId;

    private String title;

    private String content;
}
