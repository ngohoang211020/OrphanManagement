package com.orphan.api.controller.manager.Logistic.CharityEvent.dto;

import lombok.Data;

@Data
public class BenefactorCharityRequest {
    private Integer id;
    private Integer benefactorId;
    private Integer charityEventId;
    private Long donation;
    private String content;
}
