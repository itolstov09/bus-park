package dev.tolstov.buspark.model;

import dev.tolstov.buspark.TestEntityService;
import dev.tolstov.buspark.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public abstract class EntityTest {
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
        Employee dmitry = new Employee("Dmitry", "Ivanoff", 12000.0, Employee.Post.MECHANIC);
        dmitry.setHomeAddress(street);
        employeeRepository.save(dmitry);
        Employee mechanic2 = new Employee("Mechanic2", "Ivanoff", 12000.0, Employee.Post.MECHANIC);
        mechanic2.setHomeAddress(street);
        employeeRepository.save(mechanic2);
        Employee mechanic3 = new Employee("Mechanic3", "Ivanoff", 12000.0, Employee.Post.MECHANIC);
        mechanic3.setHomeAddress(street);
        employeeRepository.save(mechanic3);

        DriverLicense ivanDL = new DriverLicense("asdf1234", "D");
        Employee ivan = new Employee(
                "Ivan",
                "Ivanoff",
                15000.0,
                Employee.Post.DRIVER);
        ivan.setHomeAddress(street);
        ivan.setDriverLicense(ivanDL);
        employeeRepository.save(ivan);

        DriverLicense driverLicense = new DriverLicense("sdafsda1415", "D");
        Employee driver2 = new Employee("driver2", "lastName", 15000.0, Employee.Post.DRIVER);
        driver2.setHomeAddress(street);
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
}
