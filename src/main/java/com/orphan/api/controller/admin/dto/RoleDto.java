package com.orphan.api.controller.admin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto {
    private Integer roleId;
    private String roleName;
    private String description;
}
