package com.orphan.api.controller.manager.children.dto;

import lombok.Data;

@Data
public class ChildrenRequest {
    private Integer id;

    private String fullName;

    private String status;

    private String dateOfBirth;

    private Boolean gender;

    private String dateReceived;

    private String nameOfIntroducer;

    private String dateLeaved;

    private String nameOfNurturer;
}
