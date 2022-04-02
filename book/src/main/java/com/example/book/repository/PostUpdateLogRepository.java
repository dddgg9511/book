package com.example.book.repository;

import com.example.book.domain.PostUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostUpdateLogRepository extends JpaRepository<PostUpdateLog, Long> {
}
