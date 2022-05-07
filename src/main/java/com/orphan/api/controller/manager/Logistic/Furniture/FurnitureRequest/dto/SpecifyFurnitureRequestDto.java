package com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecifyFurnitureRequestDto {

    private Integer specifyFurnitureRequestId;

    private Integer furnitureId;

    private Integer importQuantity;

    private Integer fixQuantity;

    private String note;

    private Long totalPrice;

    private Boolean status;

}
