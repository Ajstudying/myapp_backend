package com.kaj.myapp.board.request;

import jakarta.persistence.Column;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BoardModifyRequest {

    private String title;
    private String content;
    @Column(length = 1024 * 1024 * 20)
    private String image;
    private String petname;
    private String species;

}
