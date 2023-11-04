package com.example.vital_hub.competition.data;

import java.util.List;

public class CompetitionAllDetail {
    private List<CompetitionRanking> rank;
    private CompetitionDetail detail;

    private Boolean isOwned;

    public CompetitionDetail getDetail() {
        return detail;
    }

    public List<CompetitionRanking> getRank() {
        return rank;
    }

    public Boolean getIsOwned() {
        return isOwned;
    }
}
