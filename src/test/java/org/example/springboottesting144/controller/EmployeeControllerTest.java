package org.example.springboottesting144.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springboottesting144.model.Employee;
import org.example.springboottesting144.service.EmployeeService;
import org.example.springboottesting144.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc; // niezbędne dla testów MVC, by można było wywoływać endpoint-y z testów

    @MockBean // do kontekstu aplikacji będzie dodany używany w kontrolerze
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    // stworzyć endpoint 'createEmployee' -> przekazujecie użytkownika w ciele zapytania
    @DisplayName("Testowanie endpoint 'createEmployee'")
    @Test
    void givenEmployeeObject_whenCreatedEmployee_thenReturnEmployeeAsJson() throws Exception {
        // given
        Employee employee = Employee.builder()
                .firstName("Karol")
                .lastName("Darwin")
                .email("karol@darwin.pl")
                .build();
        given(employeeService.saveEmployee(any(Employee.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

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

    // stworzyć endpoint 'getAllEmployees'
    // testowanie endpoint 'getAllEmployees'

    // stworzyć endpoint 'getEmployeeById'
    // testowanie endpoint 'getEmployeeById' - pozytywny scenariusz
    // testowanie endpoint 'getEmployeeById' - negatywny scenariusz

    // stworzyć endpoint 'updateEmployee'
    // testowanie endpoint 'updateEmployee' - pozytywny scenariusz
    // testowanie endpoint 'updateEmployee' - negatywny scenariusz

    // stworzyć endpoint 'deleteEmployee'
    // testowanie endpoint 'deleteEmployee'
}