package com.movieticket.identity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TokenRequest {
    private String grantType;
    private String email;
    private String password;
    private String refreshToken;
}