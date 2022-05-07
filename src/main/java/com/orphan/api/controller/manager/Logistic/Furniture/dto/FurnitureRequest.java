package com.orphan.api.controller.manager.Logistic.Furniture.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FurnitureRequest {

    private Integer furnitureId;

    @NotEmpty(message="{error.msg.furniture-is-required}")
    private String nameFurniture;

    @NotEmpty
    private String status;

    private Integer goodQuantity;

    private Integer brokenQuantity;

    private String image;

    private Long unitPrice;

}
