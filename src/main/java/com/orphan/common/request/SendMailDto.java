package com.orphan.common.request;

import lombok.Data;

import java.util.List;

@Data
public class SendMailDto {
    private Integer id;
    private List<String> recipients;
    private String subject;
    private String body;
    private String dateSend;
    private Boolean isAllRole;
    private List<String> roles;
    private Boolean isSendImmediately;
    private Boolean isCompleted;
    private String type;
    private Integer feedBackId;
}
