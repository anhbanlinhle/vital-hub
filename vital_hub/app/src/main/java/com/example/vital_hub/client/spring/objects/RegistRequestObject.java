package com.example.vital_hub.client.spring.objects;

import java.sql.Date;
import java.time.LocalDate;

public class RegistRequestObject {
    private String name;
    private String phoneNo;
    private String dob;
    private String sex;
    private Double currentHeight;
    private Double currentWeight;
    private Integer exerciseDaysPerWeek;
    private String description;

    private String gmail;
    private String avatar;

    public RegistRequestObject(String name,
                               String phoneNo,
                               String dob,
                               String sex,
                               Double currentHeight,
                               Double currentWeight,
                               Integer exerciseDaysPerWeek,
                               String description,
                               String gmail,
                               String avatar) {
        this.name = name;
        this.phoneNo = phoneNo;
        this.dob = dob;
        this.sex = sex;
        this.currentHeight = currentHeight;
        this.currentWeight = currentWeight;
        this.exerciseDaysPerWeek = exerciseDaysPerWeek;
        this.description = description;
        this.gmail = gmail;
        this.avatar = avatar;
    }
}
