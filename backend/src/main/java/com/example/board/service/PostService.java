package com.example.board.service;

import com.example.board.dto.PostRequest;
import com.example.board.dto.PostResponse;
import com.example.board.entity.Post;
import com.example.board.exception.PostNotFoundException;
import com.example.board.repository.PostRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public List<PostResponse> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream().map(PostResponse::from).toList();
    }

    public PostResponse findById(Long id) {
        return PostResponse.from(findPost(id));
    }

    @Transactional
    public PostResponse create(PostRequest request) {
        return PostResponse.from(repository.saveAndFlush(new Post(request.title(), request.content(), request.author())));
    }

    @Transactional
    public PostResponse update(Long id, PostRequest request) {
        Post post = findPost(id);
        post.update(request.title(), request.content(), request.author());
        return PostResponse.from(repository.saveAndFlush(post));
    }

    @Transactional
    public void delete(Long id) {
        Post post = findPost(id);
        repository.delete(post);
    }

    private Post findPost(Long id) {
        return repository.findById(id).orElseThrow(() -> new PostNotFoundException(id));
    }
}
