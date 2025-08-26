package com.example.timetable_service.dto;

public record TimeslotDTO(
        Long id,
        String dayOfWeek,
        String startTime,
        String endTime
) {}
