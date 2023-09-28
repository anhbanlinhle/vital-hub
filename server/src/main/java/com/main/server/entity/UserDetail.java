package com.main.server.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_detail")
public class UserDetail implements Serializable{

    private static final long serialVersionUID = -197553281792804396L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "current_height")
    private Double currentHeight;

    @Column(name = "current_weight")
    private Double currentWeight;

    @Column(name = "exercise_days_per_week")
    private Integer exerciseDaysPerWeek;

    @Column(name = "description")
    private String description;
}
