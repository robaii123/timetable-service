package com.example.timetable_service.dto;

import java.util.List;

public record TimetableRequest(
        List<TimeslotDTO> timeslots,
        List<FacilityDTO> facilities,
        List<CourseDTO> courses,
        List<InstructorDTO> instructors,
        List<StudentGroupDTO> studentGroups,
        List<SessionDTO> sessions
) {}
