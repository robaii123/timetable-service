package com.example.timetable_service.dto;

import java.util.List;

public record WeeklyScheduleDTO(
        String score,                 // e.g., "0hard/-5soft"
        List<SessionDTO> sessions
) {}
