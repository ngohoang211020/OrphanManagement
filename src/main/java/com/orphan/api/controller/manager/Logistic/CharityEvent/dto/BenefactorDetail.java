package com.orphan.api.controller.manager.Logistic.CharityEvent.dto;

import lombok.Data;

@Data
public class BenefactorDetail {
    private Integer id;
    private String fullName;
    private String address;
    private String phone;
    private String totalDonation;
}
