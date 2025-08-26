package com.example.timetable_service.dto;

import java.util.List;

public record WeeklyScheduleDTO(
        String score,
        List<SessionDTO> sessions
) {}
