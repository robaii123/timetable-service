package com.example.timetable_service.domain;

import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import static org.optaplanner.core.api.score.stream.Joiners.equal;

public class ELSConstraintProvider implements ConstraintProvider {

    @Override
    public Constraint[] defineConstraints(ConstraintFactory f) {
        return new Constraint[]{
                roomConflict(f),
                instructorConflict(f),
                studentGroupConflict(f)
        };
    }

    private Constraint roomConflict(ConstraintFactory f) {
        return f.forEachUniquePair(Session.class,
                        equal(Session::getTimeslot),
                        equal(Session::getFacility))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Room conflict");
    }

    private Constraint instructorConflict(ConstraintFactory f) {
        return f.forEachUniquePair(Session.class,
                        equal(Session::getTimeslot),
                        equal(Session::getInstructor))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Instructor conflict");
    }

    private Constraint studentGroupConflict(ConstraintFactory f) {
        return f.forEachUniquePair(Session.class,
                        equal(Session::getTimeslot),
                        equal(Session::getStudentGroup))
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Student group conflict");
    }

}
