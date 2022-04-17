package com.orphan.api.controller.manager.furniture.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class FurnitureRequest {

    private Integer furnitureId;

    @NotEmpty(message="{error.msg.furniture-is-required}")
    private String nameFurniture;

    @NotEmpty
    private String status;

    private Integer quantity;

    private String image;
}
