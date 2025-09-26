package com.cadt.projectmanagement.service.impl;

import com.cadt.projectmanagement.dto.request.CreatePostRequest;
import com.cadt.projectmanagement.dto.request.UpdatePostRequest;
import com.cadt.projectmanagement.dto.response.PageResponse;
import com.cadt.projectmanagement.dto.response.PostResponse;
import com.cadt.projectmanagement.exception.BadRequestException;
import com.cadt.projectmanagement.exception.NotFoundException;
import com.cadt.projectmanagement.model.Post;
import com.cadt.projectmanagement.repository.PostRepository;
import com.cadt.projectmanagement.service.PostService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.Locale;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository repo;

    public PostServiceImpl(PostRepository repo) {
        this.repo = repo;
    }

    @Override
    public PostResponse create(CreatePostRequest req) {
        String slug = toSlug(req.title());
        if (repo.existsBySlug(slug)) throw new BadRequestException("Post with same title already exits");

        Post post = Post.builder()
                .title(req.title())
                .slug(slug)
                .content(req.content())
                .status(req.status().toUpperCase(Locale.ROOT))
                .build();
        post = repo.save(post);
        return toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PostResponse getBySlug(String slug) {
        Post post = repo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        return toResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<PostResponse> list(Pageable pageable, String status) {
        Page<Post> page = (status == null || status.isBlank()) ? repo.findAll(pageable) :
                repo.findAll(Example.of(Post.builder().status(status.toUpperCase(Locale.ROOT)).build(),
                                ExampleMatcher.matching().withIgnoreNullValues()
                                        .withMatcher("status", ExampleMatcher.GenericPropertyMatchers.exact())),
                        pageable
                );

        var content = page.getContent().stream().map(this::toResponse).toList();
        return new PageResponse<>(content, page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isLast()
        );
    }

    @Override
    public PostResponse update(String slug, UpdatePostRequest req) {
        Post post = repo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        post.setTitle(req.title());
        post.setSlug(toSlug(req.title()));
        post.setContent(req.content());
        post.setStatus(req.status().toUpperCase(Locale.ROOT));
        return toResponse(post);
    }

    @Override
    public void delete(String slug) {
        Post post = repo.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Post not found"));
        repo.delete(post);
    }

    private PostResponse toResponse(Post p) {
        return new PostResponse(
                p.getId(),
                p.getTitle(),
                p.getSlug(),
                p.getContent(),
                p.getStatus(),
                p.getCreatedAt(),
                p.getUpdatedAt()
        );
    }

    private String toSlug(String input) {
        String base = Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9\\s-]", "")
                .replaceAll("\\s+", "-")
                .replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
        if (base.isBlank()) throw new BadRequestException("Invalid title for slug");
        return base;
    }


}
