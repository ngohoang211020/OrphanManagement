package com.orphan.api.controller.manager.Logistic.CharityEvent.dto;

import lombok.Data;

@Data
public class CharityRequest {
    private Integer id;
    private String charityName;
    private String content;
    private String image;
    private String title;
    private String dateStart;
    private String dateEnd;
    private Boolean isCompleted;
}
