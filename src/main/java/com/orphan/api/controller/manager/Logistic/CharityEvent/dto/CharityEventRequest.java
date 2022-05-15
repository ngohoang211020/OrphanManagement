package com.orphan.api.controller.manager.Logistic.CharityEvent.dto;

import com.orphan.common.annotation.Date;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter

public class CharityEventRequest {

    private Integer id;

    @NotEmpty(message="{error.msg.name-is-required}")
    private String charityName;
    @NotEmpty(message="{error.msg.title-is-required}")
    private String title;
    @NotEmpty(message="{error.msg.content-is-required}")
    private String content;

    private List<BenefactorRequest> benefactorRequestList;

    @NotEmpty(message="{error.msg.adoptive-Date-is-required}")
    @Date
    private String dateOfEvent;

    private String image;

}
