package org.example.springboottesting144.repository;

import org.example.springboottesting144.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

//@Repository opcjonalnie, jeśli dziedziczymy po innym Bean-ie
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByEmail(String email);

    @Query("select e from Employee e where e.firstName = ?1 and e.lastName = ?2")
    Employee findByJPQL(String firstName, String lastName);

    @Query("select e from Employee e where e.firstName = :AlaMaKota and e.lastName = :lastName")
    Employee findByJPQLNamedParams(@Param("AlaMaKota")String firstName, String lastName);

    @Query(value = "select * from employees e where e.first_name = ?1 and e.last_name = ?2", nativeQuery = true)
    Employee findByJPQLNativeSQL(String firstName, String lastName);

    @Query(value = "select * from employees e where e.first_name = :AlaMaKota and e.last_name = :lastName", nativeQuery = true)
    Employee findByJPQLNativeSQLNamedParams(@Param("AlaMaKota")String firstName, String lastName);
}
