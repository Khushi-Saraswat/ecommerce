package com.example.demo.constants;

import com.example.demo.model.RefreshToken;

import lombok.Data;

@Data
public class TokenPair {

    private String jwt;
    private RefreshToken refreshToken;
}
