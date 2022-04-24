package com.orphan.api.controller.manager.Children;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildrenCommonDto {
    private Integer childrenId;

    private String name;

    private String dateOfBirth;

    private Boolean gender;

    private String adoptiveDate;

    private String dateReceivedOfNurturer;

}