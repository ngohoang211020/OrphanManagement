package com.orphan.api.controller.manager.introducer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IntroducerDto {
    private Integer introducerId;

    private String introducerName;

    private List<String> childrens;
}
