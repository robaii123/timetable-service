package com.example.timetable_service.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@PlanningSolution
public class WeeklySchedule {

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "timeslotRange")
    private List<Timeslot> timeslots;

    @ProblemFactCollectionProperty
    @ValueRangeProvider(id = "facilityRange")
    private List<Facility> facilities;

    @ProblemFactCollectionProperty
    private List<Course> courses;

    @ProblemFactCollectionProperty
    private List<Instructor> instructors;

    @ProblemFactCollectionProperty
    private List<StudentGroup> studentGroups;

    @PlanningEntityCollectionProperty
    private List<Session> sessions;

    @PlanningScore
    private HardSoftScore score;
}
