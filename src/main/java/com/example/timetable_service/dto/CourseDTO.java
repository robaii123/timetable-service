package com.example.timetable_service.dto;

public record CourseDTO(
        Long id,
        String name,
        int weeklySessions,
        int duration,
        String requiredFacilityType
) {}
