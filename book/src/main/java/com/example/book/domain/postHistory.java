package com.example.book.domain;

import com.example.book.dto.PostUpdateRequestData;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class postHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_update_id")
    private Long id;

    private Long userId;

    private String title;

    private String content;

    @ManyToOne @JoinColumn(name = "post_id")
    @Setter
    private Posts post;

    @CreatedDate
    private LocalDateTime localDateTime;

    public static postHistory of(PostUpdateRequestData updateData){
        return postHistory.builder()
                .title(updateData.getTitle())
                .content(updateData.getContent()).build();
    }
}
