package com.orphan.api.controller.manager.HR.FeedBack.dto;

import lombok.Data;

@Data
public class FeedbackDto {
    private Integer id;
    private String email;
    private String fullName;
    private String title;
    private String content;
}
