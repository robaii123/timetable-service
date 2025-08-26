package com.example.timetable_service.service;

import com.example.timetable_service.domain.*;
import com.example.timetable_service.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MappingService {

    public WeeklySchedule toProblem(TimetableRequest req) {
        List<Timeslot> timeslots = req.timeslots().stream()
                .map(d -> new Timeslot(d.id(), d.dayOfWeek(), d.startTime(), d.endTime()))
                .collect(Collectors.toList());

        List<Facility> facilities = req.facilities().stream()
                .map(d -> new Facility(d.id(), d.name(), d.capacity(), d.type()))
                .collect(Collectors.toList());

        List<Course> courses = req.courses().stream()
                .map(d -> new Course(d.id(), d.name(), d.weeklySessions(), d.duration(), d.requiredFacilityType()))
                .collect(Collectors.toList());

        List<Instructor> instructors = req.instructors().stream()
                .map(d -> new Instructor(d.id(), d.name(), d.skill()))
                .collect(Collectors.toList());

        List<StudentGroup> groups = req.studentGroups().stream()
                .map(d -> new StudentGroup(d.id(), d.name(), d.size()))
                .collect(Collectors.toList());


        List<Session> sessions = req.sessions().stream()
                .map(d -> {

                    Course course = new Course(d.course().id(), d.course().name(), d.course().weeklySessions(),
                            d.course().duration(), d.course().requiredFacilityType());
                    Instructor instructor = new Instructor(d.instructor().id(), d.instructor().name(), d.instructor().skill());
                    StudentGroup sg = new StudentGroup(d.studentGroup().id(), d.studentGroup().name(), d.studentGroup().size());

                    Timeslot ts = (d.timeslot() == null) ? null :
                            new Timeslot(d.timeslot().id(), d.timeslot().dayOfWeek(),
                                    d.timeslot().startTime(), d.timeslot().endTime());

                    Facility fac = (d.facility() == null) ? null :
                            new Facility(d.facility().id(), d.facility().name(),
                                    d.facility().capacity(), d.facility().type());

                    return new Session(d.id(), course, instructor, sg, ts, fac);
                })
                .collect(Collectors.toList());

        WeeklySchedule schedule = new WeeklySchedule();
        schedule.setTimeslots(timeslots);
        schedule.setFacilities(facilities);
        schedule.setCourses(courses);
        schedule.setInstructors(instructors);
        schedule.setStudentGroups(groups);
        schedule.setSessions(sessions);
        return schedule;
    }

    public WeeklyScheduleDTO toDto(WeeklySchedule sol) {
        List<SessionDTO> sessionDTOs = sol.getSessions().stream()
                .map(s -> new SessionDTO(
                        s.getId(),
                        new CourseDTO(s.getCourse().getId(), s.getCourse().getName(),
                                s.getCourse().getWeeklySessions(), s.getCourse().getDuration(),
                                s.getCourse().getRequiredFacilityType()),
                        new InstructorDTO(s.getInstructor().getId(), s.getInstructor().getName(), s.getInstructor().getSkill()),
                        new StudentGroupDTO(s.getStudentGroup().getId(), s.getStudentGroup().getName(), s.getStudentGroup().getSize()),
                        s.getTimeslot() == null ? null :
                                new TimeslotDTO(s.getTimeslot().getId(), s.getTimeslot().getDayOfWeek(),
                                        s.getTimeslot().getStartTime(), s.getTimeslot().getEndTime()),
                        s.getFacility() == null ? null :
                                new FacilityDTO(s.getFacility().getId(), s.getFacility().getName(),
                                        s.getFacility().getCapacity(), s.getFacility().getType())
                ))
                .collect(Collectors.toList());

        String score = (sol.getScore() == null) ? null : sol.getScore().toString();
        return new WeeklyScheduleDTO(score, sessionDTOs);
    }
}
