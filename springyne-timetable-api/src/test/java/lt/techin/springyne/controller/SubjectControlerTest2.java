//package lt.techin.springyne.controller;
//
//import lt.techin.springyne.dto.mapper.SubjectMapper;
//import lt.techin.springyne.model.Subject;
//import lt.techin.springyne.service.SubjectService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//
//public class SubjectControlerTest2 {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private SubjectService subjectService;
//
//    @Autowired
//    private SubjectMapper subjectMapper;
//
//    @Test
//    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception{
//
//        // given - precondition or setup
//        Subject employee = Subject.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();
//        given(subjectService.saveSubject(any(Subject.class)))
//                .willAnswer((invocation)-> invocation.getArgument(0));
//
//        // when - action or behaviour that we are going test
//        ResultActions response = mockMvc.perform(post("/api/employees")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(employee)));
//
//        // then - verify the result or output using assert statements
//        response.andDo(print()).
//                andExpect(status().isCreated())
//                .andExpect(jsonPath("$.firstName",
//                        is(employee.getFirstName())))
//                .andExpect(jsonPath("$.lastName",
//                        is(employee.getLastName())))
//                .andExpect(jsonPath("$.email",
//                        is(employee.getEmail())));
//
//    }
//
//    // JUnit test for Get All employees REST API
//    @Test
//    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception{
//        // given - precondition or setup
//        List<Employee> listOfEmployees = new ArrayList<>();
//        listOfEmployees.add(Employee.builder().firstName("Ramesh").lastName("Fadatare").email("ramesh@gmail.com").build());
//        listOfEmployees.add(Employee.builder().firstName("Tony").lastName("Stark").email("tony@gmail.com").build());
//        given(employeeService.getAllEmployees()).willReturn(listOfEmployees);
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(get("/api/employees"));
//
//        // then - verify the output
//        response.andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.size()",
//                        is(listOfEmployees.size())));
//
//    }
//
//    // positive scenario - valid employee id
//    // JUnit test for GET employee by id REST API
//    @Test
//    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception{
//        // given - precondition or setup
//        long employeeId = 1L;
//        Employee employee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();
//        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(employee));
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
//
//        // then - verify the output
//        response.andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
//                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
//                .andExpect(jsonPath("$.email", is(employee.getEmail())));
//
//    }
//
//    // negative scenario - valid employee id
//    // JUnit test for GET employee by id REST API
//    @Test
//    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmpty() throws Exception{
//        // given - precondition or setup
//        long employeeId = 1L;
//        Employee employee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();
//        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employeeId));
//
//        // then - verify the output
//        response.andExpect(status().isNotFound())
//                .andDo(print());
//
//    }
//    // JUnit test for update employee REST API - positive scenario
//    @Test
//    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturnUpdateEmployeeObject() throws Exception{
//        // given - precondition or setup
//        long employeeId = 1L;
//        Employee savedEmployee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();
//
//        Employee updatedEmployee = Employee.builder()
//                .firstName("Ram")
//                .lastName("Jadhav")
//                .email("ram@gmail.com")
//                .build();
//        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.of(savedEmployee));
//        given(employeeService.updateEmployee(any(Employee.class)))
//                .willAnswer((invocation)-> invocation.getArgument(0));
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedEmployee)));
//
//
//        // then - verify the output
//        response.andExpect(status().isOk())
//                .andDo(print())
//                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
//                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
//                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));
//    }
//
//    // JUnit test for update employee REST API - negative scenario
//    @Test
//    public void givenUpdatedEmployee_whenUpdateEmployee_thenReturn404() throws Exception{
//        // given - precondition or setup
//        long employeeId = 1L;
//        Employee savedEmployee = Employee.builder()
//                .firstName("Ramesh")
//                .lastName("Fadatare")
//                .email("ramesh@gmail.com")
//                .build();
//
//        Employee updatedEmployee = Employee.builder()
//                .firstName("Ram")
//                .lastName("Jadhav")
//                .email("ram@gmail.com")
//                .build();
//        given(employeeService.getEmployeeById(employeeId)).willReturn(Optional.empty());
//        given(employeeService.updateEmployee(any(Employee.class)))
//                .willAnswer((invocation)-> invocation.getArgument(0));
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(put("/api/employees/{id}", employeeId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(updatedEmployee)));
//
//        // then - verify the output
//        response.andExpect(status().isNotFound())
//                .andDo(print());
//    }
//
//    // JUnit test for delete employee REST API
//    @Test
//    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception{
//        // given - precondition or setup
//        long employeeId = 1L;
//        willDoNothing().given(employeeService).deleteEmployee(employeeId);
//
//        // when -  action or the behaviour that we are going test
//        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", employeeId));
//
//        // then - verify the output
//        response.andExpect(status().isOk())
//                .andDo(print());
//}