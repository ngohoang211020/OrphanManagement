package com.orphan.api.controller.manager.Children.introducer.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orphan.common.annotation.Date;
import com.orphan.common.annotation.Identification;
import com.orphan.common.annotation.Phone;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IntroducerRequest {
    private Integer id;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String fullName;

    @NotEmpty(message="{error.msg.date-of-birth-is-required}")
    @Date
    private String dateOfBirth;

    @Identification
    private String identification;

    @Phone
    private String phone;

    private Boolean gender;

    private String image;

    @NotEmpty(message="{error.msg.email-is-required}")
    @Email(message="{error.msg.email-is-invalid}")
    private String email;

    private String address;

}
