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

    // In-memory job tracking (swap to DB if needed)
    private final Map<String, JobStatus> status = new ConcurrentHashMap<>();
    private final Map<String, WeeklySchedule> results = new ConcurrentHashMap<>();

    /** Start an async solve and return a jobId immediately. */
    public String startSolve(TimetableRequest req) {
        final String jobId = UUID.randomUUID().toString();
        status.put(jobId, JobStatus.RUNNING);

        final WeeklySchedule problem = mapper.toProblem(req);

// inside startSolve(...)
        solverManager.solveAndListen(
                jobId,
                id -> problem,
                best -> results.put(jobId, best),
                finalBest -> status.put(jobId, JobStatus.DONE),
                (pid, throwable) -> status.put(jobId, JobStatus.FAILED) // optional
        );


        return jobId;
    }

    /** Get current job status. */
    public JobStatus status(String jobId) {
        if (!status.containsKey(jobId) && !results.containsKey(jobId)) {
            return JobStatus.NOT_FOUND;
        }
        return status.getOrDefault(jobId, JobStatus.RUNNING);
    }

    /** Get the final result as DTO. 202 if still running, 404 if unknown. */
    public WeeklyScheduleDTO result(String jobId) {
        JobStatus st = status(jobId);
        if (st == JobStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found: " + jobId);
        }
        if (st != JobStatus.DONE) {
            // Tell the client to keep polling
            throw new ResponseStatusException(HttpStatus.ACCEPTED, "Job not finished yet");
        }
        WeeklySchedule sol = results.get(jobId);
        if (sol == null) {
            // Shouldn’t happen, but handle gracefully
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Result missing");
        }
        return mapper.toDto(sol);
    }

    /** Ask OptaPlanner to stop early. */
    public void terminate(String jobId) {
        // Mark as failed/cancelled and signal solver
        if (!status.containsKey(jobId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Job not found: " + jobId);
        }
        solverManager.terminateEarly(jobId);
        status.put(jobId, JobStatus.FAILED);
    }
}


