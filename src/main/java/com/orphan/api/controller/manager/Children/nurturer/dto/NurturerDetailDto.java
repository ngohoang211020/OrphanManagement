package com.orphan.api.controller.manager.Children.nurturer.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NurturerDetailDto extends NurturerDto{
    private String identification;

    private String email;

    private String dateOfBirth;
}
