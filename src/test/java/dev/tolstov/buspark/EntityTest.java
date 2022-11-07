package dev.tolstov.buspark;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.AddressRepository;
import dev.tolstov.buspark.repository.BusStopRepository;
import dev.tolstov.buspark.repository.EmployeeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class EntityTest {
    @Autowired
    AddressRepository addressRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    BusStopRepository busStopRepository;

    @BeforeEach
    void beforeEach() {
        storeEntities();
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
    }

    @AfterEach
    void afterEach() {
        clearTables();
    }

    private void clearTables() {
        employeeRepository.deleteAll();
        busStopRepository.deleteAll();
        addressRepository.deleteAll();
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
        assertTrue(violationException.getRootCause().getMessage().contains("bus_stop_address_id_key"));
    }


}
