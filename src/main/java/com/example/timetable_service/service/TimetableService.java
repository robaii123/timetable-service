package com.example.timetable_service.service;

import com.example.timetable_service.domain.WeeklySchedule;
import com.example.timetable_service.dto.TimetableRequest;
import com.example.timetable_service.dto.WeeklyScheduleDTO;
import lombok.RequiredArgsConstructor;
import org.optaplanner.core.api.solver.SolverManager;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final SolverManager<WeeklySchedule, String> solverManager;
    private final MappingService mapper;
    private final Map<String, JobStatus> status = new ConcurrentHashMap<>();
    private final Map<String, WeeklySchedule> results = new ConcurrentHashMap<>();

    public String startSolve(TimetableRequest req) {
        final String jobId = UUID.randomUUID().toString();
        status.put(jobId, JobStatus.RUNNING);
        final WeeklySchedule problem = mapper.toProblem(req);
        solverManager.solveAndListen(
                jobId,
                id -> problem,
                best -> results.put(jobId, best),
                finalBest -> status.put(jobId, JobStatus.DONE),
                (pid, throwable) -> status.put(jobId, JobStatus.FAILED) // optional
        );
        return jobId;
    }

    public JobStatus status(String jobId) {
        if (!status.containsKey(jobId) && !results.containsKey(jobId)) {
            return JobStatus.NOT_FOUND;
        }
        return status.getOrDefault(jobId, JobStatus.RUNNING);
    }

    public WeeklyScheduleDTO result(String jobId) {
        JobStatus st = status(jobId);
        if (st == JobStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found: " + jobId);
        }
        if (st != JobStatus.DONE) {
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "Job not finished yet");
        }
        WeeklySchedule sol = results.get(jobId);
        if (sol == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Result missing");
        }
        return mapper.toDto(sol);
    }

    public void terminate(String jobId) {
        if (!status.containsKey(jobId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found: " + jobId);
        }
        solverManager.terminateEarly(jobId);
        status.put(jobId, JobStatus.FAILED);
    }
}


