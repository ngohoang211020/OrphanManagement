package com.orphan.api.controller.common.dto;

import lombok.Data;

@Data
public class EmailNotifyDto {
   private String fullName;
   private String email;
   private String password;
}
