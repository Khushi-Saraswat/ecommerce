package com.example.demo.response;

import com.example.demo.Common.BaseResponseDTO;
import com.example.demo.constants.Role;

import lombok.Data;

@Data
public class UserResponseDTO extends BaseResponseDTO {

    private String name;
    private String username;
    private String mobileNumber;
    private Role role;

}
