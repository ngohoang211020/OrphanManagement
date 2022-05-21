package com.orphan.common.entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "entertainmnet")

public class Entertainment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer idEntertainment;

    @Column(length = 256, nullable = false)
    private String nameEntertainment;

    @Temporal(TemporalType.DATE)
    private Date dateOfEntertainment;

    @Column(length = 256, nullable = false)
    private String address;

    @Column(length = 256)
    private String image;


}
