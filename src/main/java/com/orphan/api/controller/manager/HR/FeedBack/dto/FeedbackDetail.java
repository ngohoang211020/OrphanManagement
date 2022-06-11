package com.orphan.api.controller.manager.HR.FeedBack.dto;

import lombok.Data;

@Data
public class FeedbackDetail {
    private Integer id;
    private String email;
    private String fullName;
    private String subject;
    private String body;
    private String dateFeedback;
    private String dateReply;
    private Boolean isReplied;
}
