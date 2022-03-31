package com.orphan.api.controller.manager.children.dto;

import lombok.Data;

@Data
public class ChildrenDto {

    private Integer id;

    private String fullName;

    private byte[] imageFile;

    private String image;

    private String status;

    private String dateOfBirth;

}
