package com.example.timetable_service.service;

public enum JobStatus {
    RUNNING,   // Solver is still working
    DONE,      // Solver finished successfully
    FAILED,    // Solver failed (exception, bad data, etc.)
    NOT_FOUND  // No job with that ID exists
}

