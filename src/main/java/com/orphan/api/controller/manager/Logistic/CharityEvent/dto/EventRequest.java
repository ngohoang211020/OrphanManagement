package com.orphan.api.controller.manager.Logistic.CharityEvent.dto;

import com.orphan.common.annotation.Date;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter

public class EventRequest {

    private Integer idEvent;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String nameEvent;

    private String donors;

    @NotEmpty(message="{error.msg.adoptive-Date-is-required}")
    @Date
    private String dateOfEvent;

    private String image;

    private Integer money;

    private Integer quantity;

    private String timeOfEvent;
}
