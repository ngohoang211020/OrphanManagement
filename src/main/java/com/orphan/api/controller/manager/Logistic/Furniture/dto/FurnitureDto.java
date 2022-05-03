package com.orphan.api.controller.manager.Logistic.Furniture.dto;

import lombok.Data;

@Data
public class FurnitureDto {

    private Integer furnitureId;

    private String nameFurniture;

    private String status;

    private Integer quantity;

    private String image;

    private Integer furnitureCategoryId;

    private String categoryName;
}
