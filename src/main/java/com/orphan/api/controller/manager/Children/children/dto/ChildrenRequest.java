package com.orphan.api.controller.manager.Children.children.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.orphan.common.annotation.Date;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChildrenRequest {
    private Integer id;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String fullName;

    @NotEmpty(message="{error.msg.date-of-birth-is-required}")
    @Date
    private String dateOfBirth;

    private Boolean gender=true;

    @NotEmpty(message="{error.msg.adoptive-Date-is-required}")
    @Date
    private String introductoryDate;

    private Integer introducerId;

    private String image=null;

    private Integer nurturerId;

    private String adoptiveDate;

}
