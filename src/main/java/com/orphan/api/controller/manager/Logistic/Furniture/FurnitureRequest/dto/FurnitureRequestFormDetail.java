package com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto;

import lombok.Data;

import java.util.List;

@Data
public class FurnitureRequestFormDetail {
    private Integer furnitureRequestId;

    private Integer employeeId;

    private String employeeName;

    private List<SpecifyFurnitureRequestDto> furnitureRequestList;

    private Long totalPrice;

    private String startDate;

    private String finishDate;

    private String status;

    private Integer createdId;

}
