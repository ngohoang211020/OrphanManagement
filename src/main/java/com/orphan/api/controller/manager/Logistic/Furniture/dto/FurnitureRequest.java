package com.orphan.api.controller.manager.Logistic.Furniture.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class FurnitureRequest {

    private Integer furnitureId;

    @NotEmpty(message="{error.msg.furniture-is-required}")
    private String nameFurniture;

    @NotEmpty
    private String status;

    private Integer quantity;

    private String image;
}
