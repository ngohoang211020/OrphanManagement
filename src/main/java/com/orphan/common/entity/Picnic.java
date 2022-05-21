package com.orphan.common.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "picnic")
public class Picnic extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(length = 256, nullable = false)
    private String namePicnic;

    @Column(length = 256, nullable = false)
    private String title;

    @Temporal(TemporalType.DATE)
    private Date dateOfPicnic;

    @Column(length = 256, nullable = false)
    private String address;

    @Column(length = 256)
    private String image;

    @Column(columnDefinition = "text")
    private String content;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime from;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime to;

    private String money;

    private Boolean isCompleted=false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User personInChargeId;

}
