package dev.tolstov.buspark.model;

import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.repository.BusRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityExistsException;

import static org.junit.jupiter.api.Assertions.*;

public class BusTest extends EntityTest {
    @Autowired
    BusRepository busRepository;

    /**
     * попытка сохранить автобус с уже существующим номером вызовет исключение
     */
    @Test
    void whenSaveBusWithExistsNumberPlate_thenThrowsException() {
        assertThrows(EntityExistsException.class, () -> {
                Bus busFromDB = busService.findAll().get(0);
                String numberPlate = busFromDB.getNumberPlate();
                Bus newBus = new Bus("model", numberPlate, 20);
                busService.save(newBus);
                }
        );
    }


    //      * сотрудника без водительского удостоверения нельзя указать как водителя
    @Test
    void testAddEmployeeWithoutLicenseCannotBeBusDriver() {
        EmployeeException employeeException = assertThrows(EmployeeException.class,
                () -> {
                    Employee employee = employeeService.findAll().get(0);
                    employee.setDriverLicense(null);
                    Bus bus = busService.findAll().get(0);
                    busService.setDriver(bus, employee);
                });
        assertTrue(employeeException.getMessage().endsWith("Reason: don't have a license!"));
    }


    @Test
    void whenSaveBusWithDriverWhosAlreadyDrivesBus_thenThrowsException() {
        EmployeeException employeeException = assertThrows(
                EmployeeException.class,
                () -> {
                    Bus bus = busService.findAll().get(0);
                    Employee driver = bus.getDriver();
                    Bus newBus = new Bus("model", "np", 57);
                    newBus.setDriver(driver);
                    busService.save(newBus);
                }
        );
        assertTrue( employeeException.getMessage().contains("already drives bus"));
    }

    //      одним автобусом могут заниматься несколько механиков
    @Test
    void testOneBusCanHaveFewMechanics() {
        testEntityService.testOneBusCanHaveFewMechanics();
    }

    //    у одного механика может быть на обслуживании несколько автобусов
    @Test
    void testOneMechanicCanWorkOnFewBuses() {
        testEntityService.testOneMechanicCanWorkOnFewBuses();
    }

    @Test
    void givenBusWithDriver_whenSetDriver_thenDriverChanged() {
        //given
        Employee mechanic = employeeService.findFirstByPost(Employee.Post.MECHANIC);
        DriverLicense driverLicense = new DriverLicense("123", "D");
        mechanic.setDriverLicense(driverLicense);
        Bus bus = busService.findAll().get(0);
        //when
        Employee oldDriver = bus.getDriver();
        busService.setDriver(bus, mechanic);
        //then
        Bus busById = busService.findById(bus.getId());
        assertNotEquals(busById.getDriver(), oldDriver);
    }
}
