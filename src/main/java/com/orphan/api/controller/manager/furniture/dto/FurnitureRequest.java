package com.orphan.api.controller.manager.furniture.dto;

import lombok.Data;

@Data
public class FurnitureRequest {

    private Integer furnitureId;

    private String nameFurniture;

    private String status;

    private Integer quantity;
}
