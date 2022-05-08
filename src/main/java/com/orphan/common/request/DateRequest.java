package com.orphan.common.request;

import lombok.Data;

@Data
public class DateRequest {
    private Integer day=0;
    private Integer month=0;
    private Integer year;
}
