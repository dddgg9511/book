package com.example.book.service;

import com.example.book.domain.PostUpdateLog;
import com.example.book.dto.PostUpdateRequestData;
import com.example.book.repository.PostUpdateLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostUpdateLogService {
    private final PostUpdateLogRepository postUpdateLogRepository;

    public PostUpdateLog save(Long id, PostUpdateRequestData updateData){
        return null;
    }

    public List<PostUpdateLog> getPostUpdateLogs(Long postId){
        return null;
    }
}
