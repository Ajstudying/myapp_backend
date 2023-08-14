package com.kaj.myapp.post;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostModifyRequest {
    private String title;
    private String content;
    @Column(length = 1024 * 1024 * 20)
    private String image;
}
