package com.cadt.projectmanagement.repository;

import com.cadt.projectmanagement.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);

    boolean existsBySlug(String slug);
}
