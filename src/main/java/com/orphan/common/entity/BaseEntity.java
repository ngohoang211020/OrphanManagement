package com.orphan.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(BaseEntity.class)
public class BaseEntity {

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
