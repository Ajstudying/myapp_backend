package com.kaj.myapp.post.entity;

import com.kaj.myapp.auth.entity.User;
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

    private boolean likes;

    private long ownerId;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

}
