package com.orphan.api.controller.manager.Logistic.CharityEvent.dto;
import lombok.Data;

@Data
public class EventDto {

    private Integer idEvent;

    private String nameEvent;

    private String donors;

    private String dateOfEvent;

    private String image;

    private Integer money;

    private Integer quantity;

    private String timeOfEvent;
}
