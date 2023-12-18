package org.example.springboottesting144.service.impl;

import org.example.springboottesting144.exception.ResourceNotFoundException;
import org.example.springboottesting144.model.Employee;
import org.example.springboottesting144.repository.EmployeeRepository;
import org.example.springboottesting144.service.EmployeeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceImplTest {
    // test dla saveEmployee(Employee employee) który zwraca wyjątek
    // test dla List<Employee> getAllEmployees() - pozytywny scenariusz
    // test dla List<Employee> getAllEmployees() - negatywny scenariusz
    // test dla Optional<Employee> getEmployeeById(long id);
    // test dla Employee updateEmployee(Employee updatedEmployee);
    // test dla void deleteEmployee(long id);
//    @Mock
//    private EmployeeRepository employeeRepository;
//    @InjectMocks
//    private EmployeeServiceImpl employeeService;

    private EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
    private EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepository);
    private Employee employee;

    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .id(1L)
                .firstName("Karol")
                .lastName("Fryderyk")
                .email("karol@gmail.com")
                .build();
    }

    @DisplayName("Test dla saveEmployee(Employee employee) który zwraca wyjątek")
    @Test
    void givenEmployee_whenSavedEmployee_thenReturnedEmployee() {
        // given
        given(employeeRepository.save(employee)).willReturn(employee);
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.empty());

        // when
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee).isEqualTo(employee);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void givenExistingEmployee_whenSavedEmployee_thenThrowException() {
        // given
        given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when
        // then
        assertThrows(ResourceNotFoundException.class, () -> employeeService.saveEmployee(employee));
        verify(employeeRepository, never()).save(employee);
    }

    @DisplayName("Test dla List<Employee> getAllEmployees() - pozytywny scenariusz")
    @Test
    void givenEmployeeList_whenGetAllEmployees_thenReturnEmployeeList() {
        // given
        Employee employee2 = Employee.builder()
                .id(2L)
                .firstName("Adam")
                .lastName("Małysz")
                .email("adam@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee2));

        // when
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isNotEmpty();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("Test dla List<Employee> getAllEmployees() - negatywny scenariusz")
    @Test
    void givenEmptyEmployeeList_whenGetAllEmployees_thenReturnEmptyEmployeeList() {
        // given
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when
        List<Employee> employeeList = employeeService.getAllEmployees();

        // then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList).isEmpty();
        assertThat(employeeList.size()).isEqualTo(0);
    }

    @DisplayName("test dla Optional<Employee> getEmployeeById(long id)")
    @Test
    void givenEmployeeId_whenGetEmployeeById_ThenReturnOptionalOfEmployee(){
        // given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        // when
        Optional<Employee> optionalEmployee = employeeService.getEmployeeById(employee.getId());

        // then
        assertThat(optionalEmployee).isEqualTo(Optional.of(employee));
    }
    @DisplayName("Test dla Employee updateEmployee(Employee updatedEmployee)")
    @Test
    void givenEmployee_whenUpdated_thenReturnUpdatedEmployee (){
        // given
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setFirstName("Adam");
        employee.setLastName("Małysz");
        employee.setEmail("adam@gmail.com");
        // when
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then
        assertThat(updatedEmployee.getId()).isEqualTo(employee.getId());
        assertThat(updatedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(updatedEmployee.getLastName()).isEqualTo(employee.getLastName());
        assertThat(updatedEmployee.getEmail()).isEqualTo(employee.getEmail());

    }
    @DisplayName("Test dla void deleteEmployee(long id)")
    @Test
    void givenEmployeeId_whenDeletedEmployee_thenUseRepositoryOnce (){
        // given
        Long id = employee.getId();
        willDoNothing().given(employeeRepository).deleteById(id);

        // when
        employeeService.deleteEmployee(id);

        // then
        verify(employeeRepository, times(1)).deleteById(id);
    }
}