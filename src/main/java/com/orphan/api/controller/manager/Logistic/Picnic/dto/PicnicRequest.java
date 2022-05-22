package com.orphan.api.controller.manager.Logistic.Picnic.dto;

import com.orphan.common.annotation.Date;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class PicnicRequest {

    private Integer id;

    @NotEmpty(message = "{error.msg.name-is-required}")
    private String namePicnic;

    private String dateStart;

    private String dateEnd;

    private String title;

    private String address;

    private String image;

    private String content;

    private Long money;

    private List<Integer> personInChargeId;

    private Boolean isCompleted;
}