package com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FurnitureRequestFormDto {

    private Integer furnitureRequestId;

    private Integer employeeId;

    List<SpecifyFurnitureRequestDto> furnitureRequestList;
}
