package dev.tolstov.buspark;

import dev.tolstov.buspark.model.*;
import dev.tolstov.buspark.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DBRunner {
    private final AddressService addressService;
    private final BusService busService;
    private final BusStopService busStopService;
    private final EmployeeService employeeService;
    private final RouteService routeService;

    @Bean
    public ApplicationRunner runner() {
        return args -> {

            Address street = new Address("street", 15);
            addressService.save(street);

            Employee dmitry = new Employee("Dmitry", "Ivanoff", 12000.0, Employee.Post.MECHANIC);
            employeeService.save(dmitry, street);
            Employee mechanic2 = new Employee("Mechanic2", "Ivanoff", 12000.0, Employee.Post.MECHANIC);
            employeeService.save(mechanic2, street);
            Employee mechanic3 = new Employee("Mechanic3", "Ivanoff", 12000.0, Employee.Post.MECHANIC);
            employeeService.save(mechanic3, street);

            DriverLicense ivanDL = new DriverLicense("asdf1234", "D");
            Employee ivan = new Employee(
                    "Ivan",
                    "Ivanoff",
                    15000.0,
                    Employee.Post.DRIVER);
            employeeService.save(ivan, street, ivanDL);

            DriverLicense driverLicense1 = new DriverLicense("asdf1234555", "D");
            Employee egor = new Employee(
                    "Egor",
                    "Ivanoff",
                    15000.0,
                    Employee.Post.DRIVER);
            employeeService.save(egor, street, driverLicense1);

            DriverLicense driverLicense = new DriverLicense("sdafsda1415", "D");
            Employee driver2 = new Employee("driver2", "lastName", 15000.0, Employee.Post.DRIVER);
            employeeService.save(driver2, street, driverLicense);

            Address busStopAddress = new Address("Bus stop address", null);
            addressService.save(busStopAddress);
            BusStop busStop = new BusStop("bus stop");
            busStopService.save(busStop, busStopAddress);

            Address busStop2Address = new Address("BS_2 adr", null);
            Address busStop3Address = new Address("BS_3 adr", null);
            Address busStop4Address = new Address("BS_4 adr", null);
            addressService.saveAll(List.of(busStop2Address, busStop3Address, busStop4Address));

            BusStop busStop2 = new BusStop("Bus stop 2");
            BusStop busStop3 = new BusStop("Bus stop 3");
            busStopService.save(busStop2, busStop2Address);
            busStopService.save(busStop3, busStop3Address);

            Route route1 = new Route(1);
            route1.addBusStop(busStopService.findAll().get(0));
            route1.addBusStop(busStopService.findAll().get(1));
            routeService.save(route1);

            BusStop busStop4 = new BusStop("Bus stop 4");
            busStopService.save(busStop4, busStop4Address);
            Route route2 = new Route(51);
            route2.addBusStop(busStop4);
            route2.addBusStop(busStopService.findAll().get(0));
            routeService.save(route2);
//
//
            Bus paz = new Bus("PAZ", "125 ASD 02", 20);
            busService.save(paz, ivan, Set.of(dmitry, mechanic3));

            Bus paz2 = new Bus("PAZ", "045 NU 02", 40);
            busService.save(paz2, driver2, Set.of(dmitry, mechanic2));
        };
    }
}
