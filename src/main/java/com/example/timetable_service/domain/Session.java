package com.example.timetable_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;   // <-- add this import
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PlanningEntity
public class Session {

    @PlanningId
    private Long id;

    private Course course;
    private Instructor instructor;
    private StudentGroup studentGroup;

    @PlanningVariable(valueRangeProviderRefs = {"timeslotRange"})
    private Timeslot timeslot;

    @PlanningVariable(valueRangeProviderRefs = {"facilityRange"})
    private Facility facility;
}

