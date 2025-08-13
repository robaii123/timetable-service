package com.example.timetable_service.repository;

import com.example.timetable_service.domain.StudentGroup;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentGroupRepository extends CrudRepository<StudentGroup, Long> {
}
