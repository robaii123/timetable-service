package com.example.timetable_service.dto;

public record TimeslotDTO(
        Long id,
        String dayOfWeek,  // e.g., "MONDAY"
        String startTime,  // "09:00"
        String endTime     // "10:00"
) {}
