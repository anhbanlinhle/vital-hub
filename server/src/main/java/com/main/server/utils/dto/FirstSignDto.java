package com.main.server.utils.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FirstSignDto {

    private String name;
    private String phoneNo;
    private LocalDate dob;
    private String sex;
    private Double currentHeight;
    private Double currentWeight;
    private Integer exerciseDaysPerWeek;
    private String description;
    private String gmail;
    private String avatar;
}
