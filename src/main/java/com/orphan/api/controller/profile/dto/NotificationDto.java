package com.orphan.api.controller.profile.dto;

import lombok.Data;

@Data
public class NotificationDto {
    private Integer id;
    private String subject;
    private String content;
    private String dateSend;
    private Integer userId;
    private Integer senderId;
}

