package dev.tolstov.buspark.model;

import dev.tolstov.buspark.exception.EmployeeException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeTest extends EntityTest {


    @Test
    void testTwoEmployeesCanHaveSameAddress() {
        List<Employee> employeeList = employeeRepository.findAll();
        assertEquals(
                employeeList.get(0).getHomeAddress().getStreet(),
                employeeList.get(1).getHomeAddress().getStreet()
        );
    }

    @Test
    void testSaveEmployeeWithDriverPostAndWithoutLicenseThrowException() {
        assertThrows(EmployeeException.class,
                () -> {
                    Employee employee = new Employee(
                            "name",
                            "lastName", 1.2,
                            new Address("street", "1"),
                            Employee.Post.DRIVER);
                    testEntityService.saveEmployee(employee);
                });
    }
}
