package dev.tolstov.buspark.model;

import dev.tolstov.buspark.repository.BusRepository;
import dev.tolstov.buspark.repository.BusStopRepository;
import dev.tolstov.buspark.repository.EmployeeRepository;
import dev.tolstov.buspark.repository.RouteRepository;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DBConstraintTest extends EntityTest {
    @Autowired
    BusRepository busRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    BusStopRepository busStopRepository;

    @Autowired
    EmployeeRepository employeeRepository;


    //BUS

    @Disabled
    @Test
    void testBusNumberPlateUniqueConstraint() {
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    Bus bus = busRepository.findAll().get(0);
                    String numberPlate = bus.getNumberPlate();
                    Bus newBus = new Bus("model", numberPlate, 55);
                    busRepository.save(newBus);
                }
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("bus_number_plate_key") );
    }

    @Disabled
    @Test
    void testBusDriverUniqueConstraint() {
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    Bus bus = busRepository.findAll().get(0);
                    Employee driver = bus.getDriver();
                    Bus newBus = new Bus("model", "000 NEW 02", 56);
                    newBus.setDriver(driver);
                    busRepository.save(newBus);
                }
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("bus_driver_id_key") );
    }


    // ROUTE

    @Test
    void testRouteNumberUniqueConstraint() {
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    Route routeFromDB = routeRepository.findAll().get(0);
                    Route route = new Route(routeFromDB.getRouteNumber());
                    route.setBusStops(routeFromDB.getBusStops());
                    routeRepository.save(route);
                }
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("route_route_number_key") );

    }


    // BUS STOP


    @Test
    void testBusStopNameUniqueConstraint() {
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    BusStop busStopFromDB = busStopRepository.findAll().get(0);
                    BusStop busStop = new BusStop(busStopFromDB.getName());
                    busStop.setAddress(busStopFromDB.getAddress());
                    busStopRepository.save(busStop);
                }
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("bus_stop_name_key") );

    }


    // EMPLOYEE


    @Test
    void testEmployeeDriverLicenseIDUniqueConstraint() {
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> {
                    Employee employeeFromDB = employeeRepository.findFirstByPost(Employee.Post.DRIVER);
                    Employee employee = new Employee("name", "lastname", 1.0, employeeFromDB.getPost());
                    employee.setHomeAddress(employeeFromDB.getHomeAddress());
                    employee.setDriverLicense(employeeFromDB.getDriverLicense());
                    employeeRepository.save(employee);
                }
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("employee_driver_license_id_key") );

    }

}
