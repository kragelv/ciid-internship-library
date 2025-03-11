package io.kragelv.library.dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.kragelv.library.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    
}
