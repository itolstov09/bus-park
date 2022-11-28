package dev.tolstov.buspark.model;

import dev.tolstov.buspark.TestEntityService;
import dev.tolstov.buspark.dto.EmployeeDriverDTO;
import dev.tolstov.buspark.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
public abstract class EntityTest {
    @Autowired
    AddressService addressService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    BusStopService busStopService;

    @Autowired
    RouteService routeService;

    @Autowired
    BusService busService;

    @Autowired
    TestEntityService testEntityService;


    @BeforeEach
    void beforeEach() {
        storeEntities();
    }

    @AfterEach
    void afterEach() {
        clearTables();
    }

    private void clearTables() {
        routeService.deleteAll();
        busService.deleteAll();
        employeeService.deleteAll();
        busStopService.deleteAll();
        addressService.deleteAll();
    }

    private void storeEntities() {
        Address street = new Address("street", 15);
        addressService.save(street);

        Employee dmitry = new Employee("TDmitry", "TIvanoff", 12000.0, Employee.Post.MECHANIC);
        employeeService.save(dmitry, street);
        Employee mechanic2 = new Employee("TMechanic2", "TIvanoff", 12000.0, Employee.Post.MECHANIC);
        employeeService.save(mechanic2, street);
        Employee mechanic3 = new Employee("TMechanic3", "TIvanoff", 12000.0, Employee.Post.MECHANIC);
        employeeService.save(mechanic3, street);

        DriverLicense ivanDL = new DriverLicense("tasdf1234", "D");
        EmployeeDriverDTO ivanDTO = new EmployeeDriverDTO(
                "TIvan",
                "TIvanoff",
                15000.0,
                street,
                Employee.Post.DRIVER.name(),
                ivanDL);
        Employee ivan = employeeService.save(ivanDTO);

        DriverLicense driverLicense = new DriverLicense("tsdafsda1415", "D");
        EmployeeDriverDTO driver2DTO = new EmployeeDriverDTO(
                "Tdriver2",
                "TlastName",
                15000.0,
                street,
                Employee.Post.DRIVER.name(),
                driverLicense);
        Employee driver2 = employeeService.save(driver2DTO);

        Address busStopAddress = new Address("TBus stop address", null);
        BusStop busStop = new BusStop("Tbus stop");
        busStop.setAddress(busStopAddress);
        busStopService.save(busStop);

        Address busStop2Address = new Address("TBS_2 adr", null);
        Address busStop3Address = new Address("TBS_3 adr", null);
        Address busStop4Address = new Address("TBS_4 adr", null);

        BusStop busStop2 = new BusStop("Test Bus stop 2");
        busStop2.setAddress(busStop2Address);
        busStopService.save(busStop2);

        BusStop busStop3 = new BusStop("Test Bus stop 3");
        busStop3.setAddress(busStop3Address);
        busStopService.save(busStop3);

        Route route1 = new Route(1001);
        route1.addBusStop(busStopService.findAll().get(0));
        route1.addBusStop(busStopService.findAll().get(1));
        routeService.save(route1);

        BusStop busStop4 = new BusStop("Test Bus stop 4");
        busStop4.setAddress(busStop4Address);
        busStopService.save(busStop4);
        Route route2 = new Route(1051);
        route2.addBusStop(busStop4);
        route2.addBusStop(busStopService.findAll().get(0));
        routeService.save(route2);


        Bus paz = new Bus("PAZ", "125 TES 92", 20);
        busService.save(paz, ivan, Set.of(dmitry, mechanic3));

        Bus paz2 = new Bus("PAZ", "045 TES 92", 40);
        busService.save(paz2, driver2, Set.of(dmitry, mechanic2));

    }
}
