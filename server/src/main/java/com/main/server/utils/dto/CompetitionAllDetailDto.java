package com.main.server.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionAllDetailDto {
    private List<CompetitionRankingDto> rank;
    private CompetitionDetailDto detail;
    private Boolean isOwned;
}
