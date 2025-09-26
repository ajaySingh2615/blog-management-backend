package com.cadt.projectmanagement.controller;

import com.cadt.projectmanagement.dto.request.CreatePostRequest;
import com.cadt.projectmanagement.dto.request.UpdatePostRequest;
import com.cadt.projectmanagement.dto.response.PageResponse;
import com.cadt.projectmanagement.dto.response.PostResponse;
import com.cadt.projectmanagement.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public PostResponse create(@Valid @RequestBody CreatePostRequest req) {
        return postService.create(req);
    }

    @GetMapping("/{slug}")
    public PostResponse get(@PathVariable String slug) {
        return postService.getBySlug(slug);
    }

    @GetMapping
    public PageResponse<PostResponse> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(required = false) String status
    ) {
        var sort = "DESC".equalsIgnoreCase(direction)
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return postService.list(PageRequest.of(page, size, sort), status);
    }

    @PutMapping("/{slug}")
    public PostResponse update(@PathVariable String slug, @Valid @RequestBody UpdatePostRequest req) {
        return postService.update(slug, req);
    }

    @DeleteMapping("/{slug}")
    public void delete(@PathVariable String slug) {
        postService.delete(slug);
    }
}
