package com.orphan.api.controller.manager.Logistic.CharityEvent.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class BenefactorRequest {
    private Integer id;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String fullName;

    private String address;

    private String phone;

    private Long donation;

    private String content;

}
