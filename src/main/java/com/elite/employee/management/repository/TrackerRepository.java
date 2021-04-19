package com.elite.employee.management.repository;


import com.elite.employee.management.entity.Tracker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrackerRepository extends CrudRepository<Tracker, Long> {
}
