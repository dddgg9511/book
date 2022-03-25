package com.example.book.service;

import com.example.book.domain.Posts;
import com.example.book.dto.PostUpdateRequestDto;
import com.example.book.dto.PostsSaveRequestData;
import com.example.book.errors.PostsNotFoundException;
import com.example.book.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public List<Posts> getPosts(){
        return postRepository.findAll();
    }

    public Posts getPost(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new PostsNotFoundException(id));
    }

    public Posts save(PostsSaveRequestData saveRequestData) {
        return postRepository.save(saveRequestData.toEntity());
    }

    public void delete(Long id) {
        Posts posts = getPost(id);
        postRepository.deleteById(posts.getId());
    }

    public Posts update(long id, PostUpdateRequestDto updateDto) {
        Posts post = getPost(id);

        post.update(updateDto);

        return post;
    }
}
