package com.orphan.api.controller.manager.children.dto;

import lombok.Data;

@Data
public class ChildrenDetailDto extends ChildrenDto{
    private Boolean gender;

    private String dateReceived;

    private String nameOfIntroducer;

    private String dateLeaved;

    private String nameOfNurturer;
}
