package com.example.timetable_service.repository;

import com.example.timetable_service.domain.Timeslot;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeslotRepository extends CrudRepository<Timeslot, Long> {
}
