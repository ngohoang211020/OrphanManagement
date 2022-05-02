package com.orphan.api.controller.manager.Children.nurturer.dto;

import com.orphan.api.controller.manager.Children.ChildrenCommonDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NurturerDto {
    private Integer id;

    private String fullName;

    private String phone;

    private Boolean gender;

    private String image;

    private String address;

    private List<ChildrenCommonDto> childrens;
}
