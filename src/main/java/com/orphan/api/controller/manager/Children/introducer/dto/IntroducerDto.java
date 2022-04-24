package com.orphan.api.controller.manager.Children.introducer.dto;

import com.orphan.api.controller.manager.Children.ChildrenCommonDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IntroducerDto {
    private Integer introducerId;

    private String introducerName;

    private String phone;

    private Boolean gender;

    private String image;

    private String address;

    private List<ChildrenCommonDto> childrens;
}
