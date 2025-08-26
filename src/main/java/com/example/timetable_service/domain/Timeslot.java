package com.example.timetable_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Timeslot {
    private Long id;
    private String dayOfWeek;
    private String startTime;
    private String endTime;
}
