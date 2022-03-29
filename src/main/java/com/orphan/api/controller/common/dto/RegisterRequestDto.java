package com.orphan.api.controller.common.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class RegisterRequestDto {
    private Integer loginId;

 //   @NotEmpty(message="{error.msg.password-is-required}")
    private String password;

//    @NotEmpty(message="{error.msg.password-is-required}")
    private String confirmPassword;

    @NotEmpty(message="{error.msg.email-is-required}")
    @Email(message="{error.msg.email-is-invalid}")
    private String email;

    @NotEmpty(message="{error.msg.phone-number-is-required}")
    private String phone;

    @NotEmpty(message="{error.msg.date-of-birth-is-required}")
    private String date_of_birth;

    private Boolean gender;

    @NotEmpty(message="{error.msg.address-1-is-required}")
    private String address;

    @NotEmpty(message="{error.msg.identification-is-required}")
    private String identification;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String fullName;

    private List<String> roles;

    public RegisterRequestDto() {
    }

}
