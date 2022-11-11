package dev.tolstov.buspark.model;

import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeTest extends EntityTest {

    @Autowired
    EmployeeService employeeService;


    @Test
    void testTwoEmployeesCanHaveSameAddress() {
        List<Employee> employeeList = employeeService.findAll();
        assertEquals(
                employeeList.get(0).getHomeAddress().getStreet(),
                employeeList.get(1).getHomeAddress().getStreet()
        );
    }

//    @Test
//    void testSaveEmployeeWithDriverPostAndWithoutLicenseThrowException() {
//        assertThrows(EmployeeException.class,
//                () -> {
//                    Employee employee = new Employee(
//                            "name",
//                            "lastName", 1.2,
//                            new Address("street", "1"),
//                            Employee.Post.DRIVER);
//                    testEntityService.saveEmployee(employee);
//                });
//    }

    @Test
    void whenSaveDriverWithoutLicense_thenThrowsException() {
        assertThrows(EmployeeException.class,
                () -> {
                    Address address = new Address("s", "1");
                    addressService.save(address);
                    Employee newEmployee = new Employee(
                            "N",
                            "L",
                            12.2,
                            Employee.Post.DRIVER);
                    employeeService.save(newEmployee, address, null);
                });
    }
}
