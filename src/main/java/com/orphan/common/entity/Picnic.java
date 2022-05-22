package com.orphan.common.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


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

    @Column(length = 256, nullable = false)
    private String address;

    @Column(length = 256)
    private String image;

    @Column(columnDefinition = "text")
    private String content;

    private LocalDateTime dateStart;

    private LocalDateTime dateEnd;

    private Long money;

    private Boolean isCompleted=false;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "picnic_users", joinColumns = {
            @JoinColumn(name = "picnic_id", referencedColumnName = "id") }, inverseJoinColumns = {
            @JoinColumn(name = "user_id", referencedColumnName = "id") })
    private List<User> users;

}
