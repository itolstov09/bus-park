package dev.tolstov.buspark;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.repository.AddressRepository;
import dev.tolstov.buspark.repository.BusStopRepository;
import dev.tolstov.buspark.repository.EmployeeRepository;
import dev.tolstov.buspark.repository.RouteRepository;
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
        employeeRepository.deleteAll();
        busStopRepository.deleteAll();
        addressRepository.deleteAll();
    }

    private void storeEntities() {
        Address street = new Address("street", "15");
        addressRepository.save(street);
        Employee ivan = new Employee("Ivan", "Ivanoff", 15000.0, street, Employee.Post.DRIVER);
        Employee dmitry = new Employee("Dmitry", "Ivanoff", 12000.0, street, Employee.Post.MECHANIC);
        employeeRepository.save(ivan);
        employeeRepository.save(dmitry);

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
        System.out.println();
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


}
