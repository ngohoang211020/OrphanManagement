package com.orphan.api.controller.admin.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {

    private Integer id;

    private String email;

    private String fullName;

    private String image;

    private List<String> roles;
}
