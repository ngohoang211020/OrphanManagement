package com.orphan.api.controller.manager.Children.nurturer.dto;

import com.orphan.common.annotation.Date;
import com.orphan.common.annotation.Identification;
import com.orphan.common.annotation.Phone;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class NurturerRequest {
    private Integer nurturerId;

    private String nurturerName;

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
