package com.kaj.myapp.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Entity
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Builder.Default
    @Column(columnDefinition = "BOOLEAN DEFAULT false")
    private boolean likes = false;

    private long ownerId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
