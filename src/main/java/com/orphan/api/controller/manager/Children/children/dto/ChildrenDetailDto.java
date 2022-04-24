package com.orphan.api.controller.manager.Children.children.dto;

import lombok.Data;

@Data
public class ChildrenDetailDto extends ChildrenDto{
    private Boolean gender;

    private String dateReceived;

    private String nameOfIntroducer;

    private String adoptiveDate;

    private String nameOfNurturer;
}
