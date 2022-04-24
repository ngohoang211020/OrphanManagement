package com.orphan.api.controller.manager.Children.children.dto;

import com.orphan.common.annotation.Date;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
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

    private Integer introducerId;

    private String image;

    private Integer nurturerId;

    private String dateReceivedOfNurturer;

}
