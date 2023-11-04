package com.main.server.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompetitionModifyDto {
    private Long id;
    private String duration;
    private String startedAt;
    private String endedAt;
    private String title;
    private String background;
}
