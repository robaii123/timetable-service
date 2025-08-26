package com.example.timetable_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Long id;
    private String name;
    private int weeklySessions;
    private int duration;
    private String requiredFacilityType;
}

