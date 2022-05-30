package com.orphan.common.request;

import lombok.Data;

import java.util.List;

@Data
public class MailTemplate {
    private Integer id;
    private List<String> recipients;
    private String subject;
    private String body;
}
