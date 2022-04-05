package com.orphan.api.controller.manager.staff.dto;

import com.orphan.common.annotation.Date;
import com.orphan.common.annotation.Identification;
import com.orphan.common.annotation.Phone;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
public class StaffRequest {

    private Integer staffId;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String fullName;

    @NotEmpty(message="{error.msg.date-of-birth-is-required}")
    @Date
    private String dateOfBirth;

    @NotEmpty
    private Boolean gender;

    @NotEmpty
    private String typeStaff;

    @NotEmpty(message="{error.msg.address-1-is-required}")
    private String address;

    @NotEmpty(message="{error.msg.identification-is-required}")
    @Identification
    private String identification;

    @NotEmpty(message="{error.msg.phone-number-is-required}")
    @Phone
    private String phone;

    @NotEmpty(message="{error.msg.email-is-required}")
    @Email(message="{error.msg.email-is-invalid}")
    private String email;
}
