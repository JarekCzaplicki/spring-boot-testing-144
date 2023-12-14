package org.example.springboottesting144.repository;

import org.example.springboottesting144.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
//@SpringBootTest
class EmployeeRepositoryTest {
    // testy do zapisywania pracowników
    // pobierania wszystkich pracowników
    // pobieranie pracownika po 'Id'
    // pobieranie pracownika po 'email'
    // uaktualnianie danych pracownika
    // usuwanie pracownika
    // testy dla zapytań JPQL
    // testy dla zapytań z indeksowanymi parametrami
    // testy dla zapytań z parametryzowanymi parametrami
    // Piszemy w TDD oraz BDD
    // BDD
    //given - aktualne ustawienia, obiekty
    //when - akcja, metoda, zachowanie które testujemy
    //then - weryfikacja rezultatu
    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee;


    @BeforeEach
    public void setUp() {
        employee = Employee.builder()
                .firstName("Karol")
                .lastName("Darwin")
                .email("karol@darwin.pl")
                .build();
    }

    @DisplayName("Test zapisywania pracownika")
    @Test
    void givenEmployee_whenSaved_thenReturnSavedEmployee() {
        //given
        //when
        Employee savedEmployee = employeeRepository.save(employee);

        //then
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("Test pobierania wszystkich pracowników")
    @Test
    void givenEmployeeList_whenFindAll_thenReturnsList() {
        // given
        Employee employee1 = Employee.builder()
                .firstName("Adam")
                .lastName("Słodowy")
                .email("adam@darwin.pl")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee1);
        // when
        List<Employee> employeeList = employeeRepository.findAll();
        // then
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @DisplayName("pobieranie pracownika po 'Id'")
    @Test
    void givenEmployee_whenFindById_thenReturnEmployee(){
        // given
        employeeRepository.save(employee);

        // when
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        // then
        assertThat(employeeDB).isNotNull();
        assertThat(employeeDB).isEqualTo(employee);
    }

    @DisplayName("pobieranie pracownika po 'email'")
    @Test
    void givenEmployeeEmail_whenFindByEmail_thenReturnEmployee(){
        // given
        employeeRepository.save(employee);

        // when
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        // then
        assertThat(employeeDB).isEqualTo(employee);
    }

    @DisplayName("uaktualnianie danych pracownika")
    @Test
    void givenEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee(){
        // given
        employeeRepository.save(employee);
        Employee byId = employeeRepository.findById(employee.getId()).get();
        byId.setEmail("jarek@gmail.com");
        byId.setFirstName("Jarek");

        // when
        Employee updatedEmployee = employeeRepository.save(byId);

        // then
        assertThat(updatedEmployee.getEmail()).isEqualTo("jarek@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jarek");

    }

    @DisplayName("usuwanie pracownika")
    @Test
    void givenSavedEmployee_whenDeletedById_thenRepositoryIsEmpty(){
        //given
        employeeRepository.save(employee);

        // when
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> optionalEmployee = employeeRepository.findById(employee.getId());

        // then
        assertThat(optionalEmployee).isEmpty();
    }


    @DisplayName("testy dla zapytań JPQL")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQL(){
        // given
        employeeRepository.save(employee);
        String firstName = "Karol";
        String lastName = "Darwin";

        // when
        Employee employeeDB = employeeRepository.findByJPQL(firstName, lastName);

        // then
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("testy dla zapytań JPQL")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQLNamedParams(){
        // given
        employeeRepository.save(employee);
        String firstName = "Karol";
        String lastName = "Darwin";

        // when
        Employee employeeDB = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("testy dla zapytań JPQL")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQLNativeSQL(){
        // given
        employeeRepository.save(employee);
        String firstName = "Karol";
        String lastName = "Darwin";

        // when
        Employee employeeDB = employeeRepository.findByJPQLNativeSQL(firstName, lastName);

        // then
        assertThat(employeeDB).isNotNull();
    }

    @DisplayName("testy dla zapytań JPQL")
    @Test
    void givenFirstNameAndLastName_whenFindByJPQLNativeSQLNamedParams(){
        // given
        employeeRepository.save(employee);
        String firstName = "Karol";
        String lastName = "Darwin";

        // when
        Employee employeeDB = employeeRepository.findByJPQLNativeSQLNamedParams(firstName, lastName);

        // then
        assertThat(employeeDB).isNotNull();
    }
    // testy dla zapytań z indeksowanymi parametrami
    // testy dla zapytań z parametryzowanymi parametrami
}