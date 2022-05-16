package com.orphan.api.controller.manager.Logistic.Furniture.FurnitureRequest.dto;

import com.orphan.common.annotation.Date;
import lombok.Data;

@Data
public class ExtensionTimeDateDto {
    private Integer id;
    @Date
    private String extensionDate;
}
