package com.main.server.entity.primaryPair;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ExerciseAndCompPair implements Serializable {

    private Long exerciseId;
    private Long compeId;
}
