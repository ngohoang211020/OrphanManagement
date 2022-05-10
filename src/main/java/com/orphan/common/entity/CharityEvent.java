package com.orphan.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "charity_event")
public class CharityEvent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer EventId;

    @Column(length = 256, nullable = false)
    private String nameEvent;

    @Column(length = 256, nullable = false)
    private String donors;

    @Temporal(TemporalType.DATE)
    private Date dateOfEvent;

    @Column(length = 256)
    private String image;

    private Integer money;

    private Integer quantity;

//    @Temporal(TemporalType.TIME)
    private String timeOfEvent;


}
