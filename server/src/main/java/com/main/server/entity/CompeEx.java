package com.main.server.entity;

import com.main.server.entity.primaryPair.ExerciseAndCompPair;
import com.main.server.entity.primaryPair.FriendPair;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "compe_ex")
@IdClass(ExerciseAndCompPair.class)
public class CompeEx implements Serializable {

    private static final long serialVersionUID = -197553281792804396L;

    @Id
    @Column(name = "exercise_id")
    private Long exerciseId;

    @Id
    @Column(name = "compe_id")
    private Long compeId;
}
