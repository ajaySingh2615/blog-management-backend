package com.cadt.projectmanagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_posts_slug", columnList = "slug", unique = true)
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 160)
    private String title;

    @Column(nullable = false, length = 180, unique = true)
    private String slug;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(nullable = false, length = 40)
    private String status;  //e.g., "Draft" or "Published"

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    @PrePersist
    void onCreate(){
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate(){
        updatedAt = Instant.now();
    }
}
