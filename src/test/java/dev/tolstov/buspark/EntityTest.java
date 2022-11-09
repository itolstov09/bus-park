package dev.tolstov.buspark;

import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.*;
import dev.tolstov.buspark.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EntityTest {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    BusStopRepository busStopRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    TestEntityService testEntityService;

    @Autowired
    BusRepository busRepository;

    @BeforeEach
    void beforeEach() {
        storeEntities();
    }

    @AfterEach
    void afterEach() {
        clearTables();
    }

    private void clearTables() {
        routeRepository.deleteAll();
        busRepository.deleteAll();
        employeeRepository.deleteAll();
        busStopRepository.deleteAll();
        addressRepository.deleteAll();
    }

    private void storeEntities() {
        Address street = new Address("street", "15");
        addressRepository.save(street);
        Employee dmitry = new Employee("Dmitry", "Ivanoff", 12000.0, street, Employee.Post.MECHANIC);
        employeeRepository.save(dmitry);
        Employee mechanic2 = new Employee("Mechanic2", "Ivanoff", 12000.0, street, Employee.Post.MECHANIC);
        employeeRepository.save(mechanic2);
        Employee mechanic3 = new Employee("Mechanic3", "Ivanoff", 12000.0, street, Employee.Post.MECHANIC);
        employeeRepository.save(mechanic3);

        DriverLicense ivanDL = new DriverLicense("asdf1234", "D");
        Employee ivan = new Employee(
                "Ivan",
                "Ivanoff",
                15000.0,
                street,
                Employee.Post.DRIVER);
        ivan.setDriverLicense(ivanDL);
        employeeRepository.save(ivan);

        DriverLicense driverLicense = new DriverLicense("sdafsda1415", "D");
        Employee driver2 = new Employee("driver2", "lastName", 5.2, street, Employee.Post.DRIVER);
        driver2.setDriverLicense(driverLicense);
        employeeRepository.save(driver2);

        Address busStopAddress = new Address("Bus stop address", null);
        addressRepository.save(busStopAddress);
        BusStop busStop = new BusStop("bus stop", busStopAddress);
        busStopRepository.save(busStop);

        Address busStop2Address = new Address("BS_2 adr", null);
        Address busStop3Address = new Address("BS_3 adr", null);
        Address busStop4Address = new Address("BS_4 adr", null);
        addressRepository.saveAll(List.of(busStop2Address, busStop3Address, busStop4Address));

        BusStop busStop2 = new BusStop("Bus stop 2", busStop2Address);
        BusStop busStop3 = new BusStop("Bus stop 3", busStop3Address);
        busStopRepository.saveAll(List.of(busStop2, busStop3));

        Route route1 = new Route(1);
        route1.addBusStop(busStopRepository.findAll().get(0));
        route1.addBusStop(busStopRepository.findAll().get(1));
        routeRepository.save(route1);


        BusStop busStop4 = new BusStop("Bus stop 4", busStop4Address);
        busStopRepository.save(busStop4);
        Route route2 = new Route(51);
        route2.addBusStop(busStop4);
        route2.addBusStop(busStopRepository.findAll().get(0));
        routeRepository.save(route2);


        Bus paz = new Bus("PAZ", "125ASD02");
        paz.setDriver(ivan);
        busRepository.save(paz);
        paz.addMechanic(dmitry);
        paz.addMechanic(mechanic3);
        busRepository.save(paz);

        Bus paz2 = new Bus("PAZ", "145NUM02");
        paz2.setDriver(driver2);
        paz2.addMechanic(dmitry);
        paz2.addMechanic(mechanic2);
        busRepository.save(paz2);

    }



    @Test
    void twoEmployeesCanHaveSameAddress() {
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

    @Test
    void testAddExistingBusStopAddressToNewBusStopThrowsException() {
        BusStop busStop = busStopRepository.findAll().get(0);
        Address busStopAddress = busStop.getAddress();

        BusStop anotherBusStop = new BusStop("Another bus stop", busStopAddress);
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> busStopRepository.save(anotherBusStop)
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("bus_stop_address_id_key") );
    }

    @Test
    void testFewRoutesCanHaveSameBusStop() {
        testEntityService.testFewRoutesCanHaveSameBusStop();
    }

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
