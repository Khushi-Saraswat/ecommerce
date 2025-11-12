package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegRequest {
    private String name;
    private String mobileNumber;
    private String username;
    private String password;
    private String role;
}
