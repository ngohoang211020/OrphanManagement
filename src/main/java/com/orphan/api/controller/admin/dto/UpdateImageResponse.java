package com.orphan.api.controller.admin.dto;

import lombok.Data;

@Data
public class UpdateImageResponse {

    private Integer userId;

    private byte[] imageFile;

    private String image;

}
