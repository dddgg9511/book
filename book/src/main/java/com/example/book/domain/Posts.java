package com.example.book.domain;

import com.example.book.dto.PostUpdateRequestData;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Posts extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "post") @Lazy
    private List<postHistory> histories = new ArrayList<>();

    @Builder
    public Posts(String title, String content, String author, String email) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.email = email;
    }

    public void update(PostUpdateRequestData updateDto) {
        this.title = updateDto.getTitle();
        this.content = updateDto.getContent();

        postHistory history = postHistory.of(updateDto);
        addUpdateLog(history);
    }

    private void addUpdateLog(postHistory history){
        histories.add(history);
        history.setPost(this);
    }
}
