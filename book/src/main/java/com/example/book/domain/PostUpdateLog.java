package com.example.book.domain;

import com.example.book.dto.PostUpdateRequestData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateLog extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_update_id")
    private Long id;

    private Long userId;

    private Long postId;

    private String title;

    private String content;

    public static PostUpdateLog of(Long postId, PostUpdateRequestData updateData){
        return PostUpdateLog.builder()
                .postId(postId)
                .title(updateData.getTitle())
                .content(updateData.getContent()).build();
    }
}
