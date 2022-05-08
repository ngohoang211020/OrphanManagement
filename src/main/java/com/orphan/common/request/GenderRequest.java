package com.orphan.common.request;

import lombok.Data;

@Data
public class GenderRequest {
    private Boolean gender=true;
    private String status="ACTIVED";
}
