package com.orphan.api.controller.manager.children.dto;

import com.orphan.common.annotation.Date;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ChildrenRequest {
    private Integer id;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String fullName;

    @NotEmpty
    private String status;

    @NotEmpty(message="{error.msg.date-of-birth-is-required}")
    @Date
    private String dateOfBirth;

    private Boolean gender;

    @NotEmpty(message="{error.msg.adoptive-Date-is-required}")
    @Date
    private String adoptiveDate;
}
