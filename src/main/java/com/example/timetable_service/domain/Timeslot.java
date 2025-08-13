package com.example.timetable_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timeslot {
    private Long id;
    private String dayOfWeek; // e.g., "MONDAY"
    private String startTime; // "09:00"
    private String endTime;   // "10:00"
}
