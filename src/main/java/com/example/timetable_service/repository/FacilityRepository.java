package com.example.timetable_service.repository;

import com.example.timetable_service.domain.Facility;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends CrudRepository<Facility, Long> {
}
