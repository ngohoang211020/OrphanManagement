package com.orphan.api.controller.manager.Children;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChildrenCommonDto {
    private Integer childrenId;

    private String fullName;

    private String dateOfBirth;

    private Boolean gender;

    private String introductoryDate;

    private String adoptiveDate;

}
