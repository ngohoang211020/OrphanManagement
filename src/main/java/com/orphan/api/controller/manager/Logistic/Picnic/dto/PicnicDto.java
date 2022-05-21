package com.orphan.api.controller.manager.Logistic.Picnic.dto;

import java.util.List;
import lombok.Data;

@Data

public class PicnicDto {

    private Integer id;

    private String namePicnic;

    private String address;

    private String dateOfPicnic;

    private String image;

    private String title;

    private Boolean isCompleted;

}
