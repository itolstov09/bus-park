package dev.tolstov.buspark.model;

import dev.tolstov.buspark.exception.EmployeeException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class BusTest extends EntityTest {
    // Тесты для автобуса
//      номерной знак(уникальность) нельзя сохранить автобус с номерным
//      знаком, который уже есть в базе
    @Test
    void testAddBusWithExistingNumberPlateThrowsException() {
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    Bus bus = busService.findAll().get(0);
                    String numberPlate = bus.getNumberPlate();
                    Bus newBus = new Bus("model", numberPlate);
                    busService.save(newBus);
                }
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("bus_number_plate_key") );
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
    void testBusDriverUniqueConstraint() {
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    Bus bus = busService.findAll().get(0);
                    Employee driver = bus.getDriver();
                    Bus newBus = new Bus("model", "np");
                    newBus.setDriver(driver);
                    busService.save(newBus);
                }
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("bus_driver_id_key") );
    }

    @Test
    void whenSetDriverWhosAlreadyDrivesBus_thenThrowsException() {
        EmployeeException employeeException = assertThrows(
                EmployeeException.class,
                () -> {
                    Bus bus = busService.findAll().get(0);
                    Employee driver = bus.getDriver();
                    Bus newBus = new Bus("model", "np");
                    busService.setDriver(newBus, driver);
                }
        );
        assertTrue( employeeException.getMessage().startsWith("Driver already drives bus"));
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
