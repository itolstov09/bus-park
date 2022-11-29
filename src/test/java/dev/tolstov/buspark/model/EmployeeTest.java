package dev.tolstov.buspark.model;

import dev.tolstov.buspark.dto.EmployeeDriverDTO;
import dev.tolstov.buspark.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolationException;
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

    @Test
    void whenSaveDriverWithoutLicense_thenThrowsException() {
        assertThrows(ConstraintViolationException.class,
                () -> {
                    Address address = new Address("s", 1);
                    Employee newEmployee = new Employee(
                            "N",
                            "L",
                            12.2,
                            Employee.Post.DRIVER);
                    EmployeeDriverDTO driverDTO = new EmployeeDriverDTO();
                    newEmployee.setHomeAddress(address);
                    BeanUtils.copyProperties(newEmployee, driverDTO);
                    driverDTO.setPost(Employee.Post.DRIVER.name());
                    employeeService.createDriver(driverDTO);
                });
    }

    /**
     * При сохранении сотрудника в адресе должен быть указан номер квартиры
     */
    @Test
    void whenSaveEmployeeWithoutApartmentNumber_thenThrowsException(){
        assertThrows(ConstraintViolationException.class,
                () -> {
                    Employee newEmployee = new Employee(
                            "N",
                            "L",
                            12.2,
                            Employee.Post.MECHANIC);
                    employeeService.save(newEmployee,
                            new Address("s", null));
                } );
    }
}
