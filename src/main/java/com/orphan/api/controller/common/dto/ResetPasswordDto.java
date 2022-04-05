package com.orphan.api.controller.common.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ResetPasswordDto {
    private Integer loginId;

    @NotEmpty
    @Email
    private String email;
}
