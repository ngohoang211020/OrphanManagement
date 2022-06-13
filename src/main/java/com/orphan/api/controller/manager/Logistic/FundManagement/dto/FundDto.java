package com.orphan.api.controller.manager.Logistic.FundManagement.dto;

import lombok.Data;

@Data
public class FundDto {

    private Integer id;

    private String date;

    private Long money;

    private String description;

    private String type;

    private Integer userId;

    private Boolean isCalculated = false;
}
