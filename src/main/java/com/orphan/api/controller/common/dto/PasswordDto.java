package com.orphan.api.controller.common.dto;

import com.orphan.common.annotation.Password;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class PasswordDto {
    @NotEmpty
    @Password
    private String newPassWord;

    @NotEmpty
    private String confirmPassWord;
}
