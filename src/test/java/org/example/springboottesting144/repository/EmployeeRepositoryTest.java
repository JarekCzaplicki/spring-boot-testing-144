package org.example.springboottesting144.repository;

import org.example.springboottesting144.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
        assertThat(savedEmployee.getId()).isBetween(0L, 2L);
        assertThat(savedEmployee.getId()).isGreaterThan(0);
    }

    @DisplayName("Test pobierania wszystkich pracowników")
    @Test
    void x() {
        // given
        // when
        // then
    }
}