package org.example.springboottesting144.repository;

import org.example.springboottesting144.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository opcjonalnie, jeśli dziedziczymy po innym Bean-ie
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
