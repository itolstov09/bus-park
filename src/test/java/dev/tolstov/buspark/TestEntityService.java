package dev.tolstov.buspark;

import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.service.BusService;
import dev.tolstov.buspark.service.EmployeeService;
import dev.tolstov.buspark.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Service
@Import(EntityTestConfig.class)
public class TestEntityService {
    @Autowired
    EmployeeService employeeService;

    @Autowired
    RouteService routeService;

    @Autowired
    BusService busService;


    @Transactional
    public void testFewRoutesCanHaveSameBusStop() {
        Route route = routeService.findAll().get(1);

        Route newRoute = new Route(40);
        routeService.save(newRoute, Set.of(route.getBusStops().iterator().next()));

        Route savedRoute = routeService.findById(newRoute.getId());

        assertTrue(savedRoute.getBusStops().contains(
                route.getBusStops().iterator().next()
        ));
    }


    @Transactional
    public void testOneBusCanHaveFewMechanics() {
        Bus bus = busService.findAll().get(0);
        assertTrue(bus.getMechanics().size() > 1);
    }

    public void testOneMechanicCanWorkOnFewBuses() {
        Employee mechanic = employeeService.findFirstByPost(Employee.Post.MECHANIC);
        assertTrue(busService.findBusesByMechanicId(mechanic.getId()).size() > 1);
    }
}
