package com.orphan.api.controller.manager.Children.introducer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IntroducerDetailDto extends IntroducerDto
{

    private String identification;

    private String email;

    private String dateOfBirth;
}
