package com.orphan.api.controller.manager.introducer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntroducerDetailDto extends IntroducerDto
{
    private Boolean gender;

    private String address;

    private String identification;

    private String phone;

    private String email;


}
