package com.orphan.api.controller.manager.Logistic.CharityEvent.dto;
import lombok.Data;

import java.util.List;

@Data
public class CharityEventDetailDto {

    private Integer id;
    private String charityName;
    private String title;
    private String content;

    private List<BenefactorCharityRequest> benefactorCharityRequestList;
    private String dateStart;
    private String dateEnd;
    private String image;
    private Long totalDonation;
    private String status;
    private Boolean isCompleted;
}
