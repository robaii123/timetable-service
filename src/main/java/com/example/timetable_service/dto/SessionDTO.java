package com.example.timetable_service.dto;

public record SessionDTO(
        Long id,
        CourseDTO course,
        InstructorDTO instructor,
        StudentGroupDTO studentGroup,
        TimeslotDTO timeslot,
        FacilityDTO facility
) {}
