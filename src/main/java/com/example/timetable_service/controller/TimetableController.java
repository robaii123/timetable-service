package com.example.timetable_service.controller;

import com.example.timetable_service.dto.TimetableRequest;
import com.example.timetable_service.dto.WeeklyScheduleDTO;
import com.example.timetable_service.service.TimetableService;
import com.example.timetable_service.service.JobStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/planning")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService service;

    @PostMapping("/solve")
    public Map<String, String> solve(@RequestBody TimetableRequest req) {
        String jobId = service.startSolve(req);
        return Map.of("jobId", jobId);
    }

    @GetMapping("/status/{jobId}")
    public Map<String, String> status(@PathVariable String jobId) {
        JobStatus st = service.status(jobId);
        return Map.of("status", st.name());
    }

    @GetMapping("/result/{jobId}")
    public WeeklyScheduleDTO result(@PathVariable String jobId) {
        return service.result(jobId);
    }

    @PostMapping("/terminate/{jobId}")
    public void terminate(@PathVariable String jobId) {
        service.terminate(jobId);
    }
}

