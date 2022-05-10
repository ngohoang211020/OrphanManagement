package com.orphan.api.controller.manager.Children.children.dto;

import lombok.Data;

@Data
public class ChildrenDetailDto extends ChildrenDto{
    private Boolean gender;

    private String adoptiveDate;

    private String nameOfIntroducer;

    private String introductoryDate;

    private String nameOfNurturer;

    private Integer introducerId;

    private Integer nurturerId;
}
