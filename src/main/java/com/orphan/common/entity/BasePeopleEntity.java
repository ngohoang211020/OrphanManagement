package com.orphan.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(BasePeopleEntity.class)
public class BasePeopleEntity {

    @Column(unique = true)
    public String email;

    @Column(length = 128, nullable = false)
    public String fullName;

    public String phone;

    public Boolean gender;

    public String address;

    @Temporal(TemporalType.DATE)
    public Date dateOfBirth;

    public String identification;

    @Column(columnDefinition = "text default null")
    private String image;

    @Column(name = "created_at")
    @CreationTimestamp
    public LocalDateTime createdAt;

    @Column(name = "created_id")
    public String createdId;

    @Column(name = "modified_at")
    @UpdateTimestamp
    public LocalDateTime modifiedAt;

    @Column(name = "modified_id")
    public String modifiedId;
}
