package com.example.timetable_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Facility {
    private Long id;
    private String name;
    private int capacity;
    private String type;
}

