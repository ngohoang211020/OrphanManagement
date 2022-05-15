package com.orphan.api.controller.manager.Logistic.CharityEvent.dto;

import lombok.Data;

import java.util.List;

@Data
public class BenefactorListRequest {
    private Integer id;
    private List<BenefactorRequest> benefactorRequestList;
    private Integer charityEventId;
}
