package com.orphan.api.controller.manager.Logistic.Furniture.dto;

import lombok.Data;

@Data
public class FurnitureDto {

    private Integer furnitureId;

    private String nameFurniture;

    private Integer goodQuantity;

    private Integer brokenQuantity;

    private String image;

    private String status;

    private Long unitPrice;

}