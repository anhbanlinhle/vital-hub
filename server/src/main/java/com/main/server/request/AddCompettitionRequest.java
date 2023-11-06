package com.main.server.request;

import com.main.server.utils.enums.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCompettitionRequest {
    private String title;
    private String background;
    private String type;
    private String startedAt;
    private String endedAt;
    private String duration;
}
