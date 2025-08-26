package com.example.timetable_service.repository;

import com.example.timetable_service.domain.Instructor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstructorRepository extends CrudRepository<Instructor, Long> {
}
