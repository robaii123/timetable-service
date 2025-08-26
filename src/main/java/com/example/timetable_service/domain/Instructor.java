package com.example.timetable_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Instructor {
    private Long id;
    private String name;
    private String skill;
}

