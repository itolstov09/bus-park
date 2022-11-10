package dev.tolstov.buspark.model;

import dev.tolstov.buspark.exception.EmployeeException;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BusTest extends EntityTest {
    // Тесты для автобуса
//      номерной знак(уникальность) нельзя сохранить автобус с номерным
//      знаком, который уже есть в базе
    @Test
    void testAddBusWithExistingNumberPlateThrowsException() {
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    Bus bus = busRepository.findAll().get(0);
                    String numberPlate = bus.getNumberPlate();
                    Bus newBus = new Bus("model", numberPlate);
                    busRepository.save(newBus);
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
                    Employee employee = employeeRepository.findAll().get(0);
                    employee.setDriverLicense(null);
                    Bus bus = busRepository.findAll().get(0);
                    bus.setDriver(employee);
                });
        assertTrue(employeeException.getMessage().endsWith("Reason: don't have a license!"));
    }

    @Test
    void testBusDriverMustBeUnique() {
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    Bus bus = busRepository.findAll().get(0);
                    Employee driver = bus.getDriver();
                    Bus newBus = new Bus("model", "np");
                    newBus.setDriver(driver);
                    busRepository.save(newBus);
                }
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("bus_driver_id_key") );
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
}
