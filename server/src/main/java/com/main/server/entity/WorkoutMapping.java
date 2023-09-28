package com.main.server.entity;

import com.main.server.utils.enums.BodyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "workout_mapping")
public class WorkoutMapping implements Serializable {

    private static final long serialVersionUID = -197553281792804396L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bmi_upper_bound")
    private Float bmiUpperBound;

    @Column(name = "bmi_lower_bound")
    private Float bmiLowerBound;

    @Column(name = "body_status")
    @Enumerated(EnumType.STRING)
    private BodyStatus bodyStatus;

    @Column(name = "suggest_group_id")
    private Long suggestGroupId;
}
