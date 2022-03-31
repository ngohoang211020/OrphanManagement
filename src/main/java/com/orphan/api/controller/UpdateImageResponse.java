package com.orphan.api.controller;

import lombok.Data;

@Data
public class UpdateImageResponse {

    private Integer Id;

    private byte[] imageFile;

    private String image;

}
