package com.orphan.api.controller.manager.Logistic.Entertainment.dto;
import com.orphan.common.annotation.Date;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter

public class EntertainmentRequest {

    private Integer idEntertainment;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String nameEntertainment;

    @NotEmpty(message="{error.msg.adoptive-Date-is-required}")
    @Date
    private String dateOfEntertainment;

    private String address;

    private String image;



}
