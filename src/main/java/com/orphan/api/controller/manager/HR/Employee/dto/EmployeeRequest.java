package com.orphan.api.controller.manager.HR.Employee.dto;

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
public class EmployeeRequest {
    private Integer id;

    @NotEmpty(message="{error.msg.email-is-required}")
    @Email(message="{error.msg.email-is-invalid}")
    private String email;

    @NotEmpty(message="{error.msg.phone-number-is-required}")
    @Phone
    private String phone;

    @NotEmpty(message="{error.msg.date-of-birth-is-required}")
    @Date
    private String dateOfBirth;

    private Boolean gender;

    @NotEmpty(message="{error.msg.address-1-is-required}")
    private String address;

    @NotEmpty(message="{error.msg.identification-is-required}")
    @Identification
    private String identification;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String fullName;

    private String image;

    public EmployeeRequest() {
    }
}
