package com.orphan.api.controller.manager.staff.dto;

import lombok.Data;

@Data
public class StaffDetailDto extends StaffDto{
    private Boolean gender;

    private String address;

    private String identification;

    private String email;

    private String dateOfBirth;
}
