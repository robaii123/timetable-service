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
    private int weeklySessions; // how many sessions per week
    private int duration;       // minutes per session
    private String requiredFacilityType; // e.g., "LAB", "CLASSROOM"
}

