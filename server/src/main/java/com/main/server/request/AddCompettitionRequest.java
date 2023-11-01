package com.main.server.request;

import com.main.server.utils.enums.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCompettitionRequest {
    private String title;
    private ExerciseType type;
    private String background;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private Time duration;
}
