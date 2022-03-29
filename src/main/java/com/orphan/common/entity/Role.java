package com.orphan.common.entity;

import com.orphan.enums.ERole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "roles")
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false, unique = true)
    private ERole name;

    @Column(name = "created_at")
    @CreationTimestamp
    public LocalDateTime createdAt;

    @Column(name = "modified_at")
    @UpdateTimestamp
    public LocalDateTime modifiedAt;

    public Role(Integer id, ERole name) {
        this.id = id;
        this.name = name;
    }

    public String getName(){
        return name.toString();
    }
}
