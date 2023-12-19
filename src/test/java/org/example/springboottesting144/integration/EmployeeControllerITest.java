package org.example.springboottesting144.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springboottesting144.model.Employee;
import org.example.springboottesting144.repository.EmployeeRepository;
import org.example.springboottesting144.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class EmployeeControllerITest {

    @Autowired
    private MockMvc mockMvc; // niezbędne dla testów MVC, by można było wywoływać endpoint-y z testów

//    @MockBean // do kontekstu aplikacji będzie dodany używany w kontrolerze
//    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmployeeRepository employeeRepository;

    // Musimy czyścić prawdziwe repozytorium
    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
    }

    // stworzyć endpoint 'createEmployee' -> przekazujecie użytkownika w ciele zapytania
    @DisplayName("Testowanie endpoint 'createEmployee'")
    @Test
    void givenEmployeeObject_whenCreatedEmployee_thenReturnEmployeeAsJson() throws Exception {
        // given
        Employee employee = Employee.builder()
                .id(1L) // powinien sobie radzić bez pola ID
                .firstName("Karol")
                .lastName("Darwin")
                .email("karol@darwin.pl")
                .build();

        // when
        ResultActions response = mockMvc.perform(post("/api/employees") // wywołujemy statycznę metodę 'post' jeśli wejdziecie do 'MockMvcRequestBuilders' to zobaczycie wszystkie możliwe czasowniki HTTP
                .contentType(MediaType.APPLICATION_JSON) // typ zawartości odpowiedzi czyli JSON
                .content(objectMapper.writeValueAsString(employee))); //jaki obiekt jest jako JSON

        // then
        response.andDo(print()) // dzięki temu na konsoli jest drukowana pełna informacja
                .andExpect(status().isCreated()) // oczekujemy że metoda zwraca w odpowiedzi status 'utworzono'
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    @DisplayName("stworzyć endpoint 'getAllEmployees'")
    @Test
    void givenListOfEmployees_whenGetAllEmployees_thenReturnedJsonListSizeIsEqualToListOfEmployees() throws Exception {
        // given
        List<Employee> listOfEmployee = new ArrayList<>();
        listOfEmployee.add(Employee.builder().firstName("Karol").lastName("Darwin").email("karol@darwin.pl").build());
        listOfEmployee.add(Employee.builder().firstName("Adam").lastName("Luter").email("adam@darwin.pl").build());
        employeeRepository.saveAll(listOfEmployee);

        // when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", // sprawdzić jak pobierać pierwszy element z listy
                        is(listOfEmployee.size())))
                .andExpect(jsonPath("$[0].size()", is(4)))
                .andExpect(jsonPath("$[0].firstName", is(listOfEmployee.get(0).getFirstName())))
                .andExpect(jsonPath("$[0].lastName", is(listOfEmployee.get(0).getLastName())))
                .andExpect(jsonPath("$[0].email", is(listOfEmployee.get(0).getEmail())));
    }

    @DisplayName("stworzyć endpoint 'getAllEmployees' pusta lista")
    @Test
    void givenEmptyListOfEmployees_whenGetAllEmployees_thenReturnedJsonListSizeIsEqual0() throws Exception {
        // give
        // when
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(0)));
    }

    @DisplayName("stworzyć endpoint 'getEmployeeById' - pozytywny scenariusz")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeAsJson() throws Exception {
        // given
        Employee employee = Employee.builder().firstName("Karol").lastName("Darwin").email("karol@darwin.pl").build();
        employeeRepository.save(employee);

        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));

    }

    @DisplayName("stworzyć endpoint 'getEmployeeById' - negatywny scenariusz")
    @Test
    void givenEmployeeId_whenGetEmployeeById_thenReturnEmptyOptional() throws Exception {
        // given
        // when
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 1L));

        // then
        response.andDo(print())
                .andExpect(status().isNotFound());
    }


    @DisplayName("testowanie endpoint 'updateEmployee' korzysta z metody 'save' - pozytywny scenariusz")
    @Test
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given
        Employee savedEmployee = Employee.builder().firstName("Karol").lastName("Darwin").email("karol@darwin.pl").build();
        Employee updatedEmployee = Employee.builder().firstName("Adam").lastName("Luter").email("adam@darwin.pl").build();
        employeeRepository.save(savedEmployee);

        // when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
    }

    @DisplayName("testowanie endpoint 'updateEmployee' korzysta z metody 'save' - negatywny scenariusz")
    @Test
    void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception {
        // given
        long id = 1L;
        Employee updatedEmployee = Employee.builder().firstName("Adam").lastName("Luter").email("adam@darwin.pl").build();

        // when
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));

        // then
        response.andDo(print()).andExpect(status().isNotFound());
    }


    @DisplayName("testowanie endpoint 'deleteEmployee'")
    @Test
    void givenEmployeeId_whenDeleteEmployee_thenReturnOK() throws Exception {
        // given
        Employee savedEmployee = Employee.builder().firstName("Karol").lastName("Darwin").email("karol@darwin.pl").build();
        employeeRepository.save(savedEmployee);

        // when
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        // then
        response.andDo(print()).andExpect(status().isOk());
    }

}