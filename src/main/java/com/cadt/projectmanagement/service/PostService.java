package com.cadt.projectmanagement.service;

import com.cadt.projectmanagement.dto.request.CreatePostRequest;
import com.cadt.projectmanagement.dto.request.UpdatePostRequest;
import com.cadt.projectmanagement.dto.response.PageResponse;
import com.cadt.projectmanagement.dto.response.PostResponse;
import org.springframework.data.domain.Pageable;

public interface PostService {
    PostResponse create(CreatePostRequest req);

    PostResponse getBySlug(String slug);

    PageResponse<PostResponse> list(Pageable pageable, String status);  //optional filter

    PostResponse update(String slug, UpdatePostRequest request);

    void delete(String slug);
}