package com.kaj.myapp.auth.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProfileModifyRequest {
    private String petname;
    private String species;
}
