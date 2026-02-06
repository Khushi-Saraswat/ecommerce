package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String name;
    private String username;
    private String password;
    private String mobileNumber;
    private String city;
    private String state;
    private String role;

}
