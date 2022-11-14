package dev.tolstov.buspark.model;

import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void whenSaveDriverWithoutLicense_thenThrowsException() {
        assertThrows(EmployeeException.class,
                () -> {
                    Address address = new Address("s", 1);
                    addressService.save(address);
                    Employee newEmployee = new Employee(
                            "N",
                            "L",
                            12.2,
                            Employee.Post.DRIVER);
                    employeeService.save(newEmployee, address, null);
                });
    }

    /**
     * При сохранении сотрудника в адресе должен быть указан номер квартиры
     */
    @Test
    void whenSaveEmployeeWithoutApartmentNumber_thenThrowsException(){
        EmployeeException employeeException = assertThrows(EmployeeException.class,
                () -> {
                    Address address = new Address("s", null);
                    addressService.save(address);
                    Employee newEmployee = new Employee(
                            "N",
                            "L",
                            12.2,
                            Employee.Post.MECHANIC);
                    employeeService.save(newEmployee, address);
                });
        assertEquals("Employee home address must have apartment number!", employeeException.getMessage());
    }
}
