package dev.tolstov.buspark.model;

import dev.tolstov.buspark.TestEntityService;
import dev.tolstov.buspark.dto.BusDTO;
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
    AddressServiceImpl addressServiceImpl;

    @Autowired
    EmployeeServiceImpl employeeServiceImpl;

    @Autowired
    BusStopServiceImpl busStopServiceImpl;

    @Autowired
    RouteServiceImpl routeServiceImpl;

    @Autowired
    BusServiceImpl busServiceImpl;

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
        routeServiceImpl.deleteAll();
        busServiceImpl.deleteAll();
        employeeServiceImpl.deleteAll();
        busStopServiceImpl.deleteAll();
        addressServiceImpl.deleteAll();
    }

    private void storeEntities() {
        Address street = new Address("street", 15);
        addressServiceImpl.save(street);

        Employee dmitry = new Employee("TDmitry", "TIvanoff", 12000.0, Employee.Post.MECHANIC);
        employeeServiceImpl.save(dmitry, street);
        Employee mechanic2 = new Employee("TMechanic2", "TIvanoff", 12000.0, Employee.Post.MECHANIC);
        employeeServiceImpl.save(mechanic2, street);
        Employee mechanic3 = new Employee("TMechanic3", "TIvanoff", 12000.0, Employee.Post.MECHANIC);
        employeeServiceImpl.save(mechanic3, street);

        DriverLicense ivanDL = new DriverLicense("tasdf1234", "D");
        EmployeeDriverDTO ivanDTO = new EmployeeDriverDTO(
                "TIvan",
                "TIvanoff",
                15000.0,
                street,
                Employee.Post.DRIVER.name(),
                ivanDL);
        Employee ivan = employeeServiceImpl.createDriver(ivanDTO);

        DriverLicense driverLicense = new DriverLicense("tsdafsda1415", "D");
        EmployeeDriverDTO driver2DTO = new EmployeeDriverDTO(
                "Tdriver2",
                "TlastName",
                15000.0,
                street,
                Employee.Post.DRIVER.name(),
                driverLicense);
        Employee driver2 = employeeServiceImpl.createDriver(driver2DTO);

        Address busStopAddress = new Address("TBus stop address", null);
        BusStop busStop = new BusStop("Tbus stop");
        busStop.setAddress(busStopAddress);
        busStopServiceImpl.save(busStop);

        Address busStop2Address = new Address("TBS_2 adr", null);
        Address busStop3Address = new Address("TBS_3 adr", null);
        Address busStop4Address = new Address("TBS_4 adr", null);

        BusStop busStop2 = new BusStop("Test Bus stop 2");
        busStop2.setAddress(busStop2Address);
        busStopServiceImpl.save(busStop2);

        BusStop busStop3 = new BusStop("Test Bus stop 3");
        busStop3.setAddress(busStop3Address);
        busStopServiceImpl.save(busStop3);

        Route route1 = new Route(1001);
        route1.addBusStop(busStopServiceImpl.findAll().get(0));
        route1.addBusStop(busStopServiceImpl.findAll().get(1));
        routeServiceImpl.save(route1);

        BusStop busStop4 = new BusStop("Test Bus stop 4");
        busStop4.setAddress(busStop4Address);
        busStopServiceImpl.save(busStop4);
        Route route2 = new Route(1051);
        route2.addBusStop(busStop4);
        route2.addBusStop(busStopServiceImpl.findAll().get(0));
        routeServiceImpl.save(route2);


        // TODO переписать с использованием CRUD
        BusDTO pazDTO = new BusDTO();
        pazDTO.setModel("PAZ");
        pazDTO.setNumberPlate("125 TES 92");
        pazDTO.setMaxPassenger(20);
        Bus paz = busServiceImpl.create(pazDTO);
        busServiceImpl.setBusDriver(ivan.getId(), paz.getId());
        busServiceImpl.addMechanic(dmitry.getId(), paz.getId());
        busServiceImpl.addMechanic(mechanic3.getId(), paz.getId());

        BusDTO paz2DTO = new BusDTO();
        paz2DTO.setModel("PAZ");
        paz2DTO.setNumberPlate("045 TES 92");
        paz2DTO.setMaxPassenger(40);
        Bus paz2 = busServiceImpl.create(paz2DTO);
        busServiceImpl.setBusDriver(driver2.getId(), paz2.getId());
        busServiceImpl.addMechanic(dmitry.getId(), paz2.getId());
        busServiceImpl.addMechanic(mechanic2.getId(), paz2.getId());

    }
}
