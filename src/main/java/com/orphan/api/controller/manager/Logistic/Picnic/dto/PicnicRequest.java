package com.orphan.api.controller.manager.Logistic.Picnic.dto;

import com.orphan.common.annotation.Date;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class PicnicRequest {

    private Integer id;

    @NotEmpty(message = "{error.msg.name-is-required}")
    private String namePicnic;

    @NotEmpty(message = "{error.msg.date-is-required}")
    @Date
    private String dateOfPicnic;

    private String title;

    private String address;

    private String image;

    private String content;

    private String from;

    private String to;

    private String money;

    private Integer personInChargeId;

    private Boolean isCompleted;
}
