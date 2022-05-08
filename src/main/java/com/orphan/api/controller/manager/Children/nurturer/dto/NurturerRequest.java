package com.orphan.api.controller.manager.Children.nurturer.dto;

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

public class NurturerRequest {
    private Integer id;

    private String fullName;

    @NotEmpty(message="{error.msg.date-of-birth-is-required}")
    @Date
    private String dateOfBirth;

    @Identification
    private String identification;

    @Phone
    private String phone;

    private Boolean gender=true;

    private String image=null;

    @NotEmpty(message="{error.msg.email-is-required}")
    @Email(message="{error.msg.email-is-invalid}")
    private String email;

    private String address;
}
