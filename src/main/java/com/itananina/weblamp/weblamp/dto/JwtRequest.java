package com.itananina.weblamp.weblamp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {
    private String username;
    private String password;
    private String email;

    public JwtRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
