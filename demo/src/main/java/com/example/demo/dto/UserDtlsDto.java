package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDtlsDto {
    private String name;
    private String mobileNumber;
    private String username;
    private String password;
    private String role;

    public UserDtlsDto(String name, String mobileNumber, String username, String password, String role) {
        this.name = name;
        this.mobileNumber = mobileNumber;
        this.username = username;
        this.password = password;
        this.role = role;
    }

}
