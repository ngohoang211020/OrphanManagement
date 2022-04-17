package com.orphan.api.controller.admin.dto;

import lombok.Data;

@Data
public class UserDetailDto extends UserDto {

    private String phone;

    private String identification;

    private String address;

    private Boolean gender;

    private String date_of_birth;

}
