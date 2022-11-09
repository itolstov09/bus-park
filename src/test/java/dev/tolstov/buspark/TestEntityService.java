package dev.tolstov.buspark;

import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.repository.BusRepository;
import dev.tolstov.buspark.repository.EmployeeRepository;
import dev.tolstov.buspark.repository.RouteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Service
@Import(EntityTestConfig.class)
public class TestEntityService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    BusRepository busRepository;


    @Transactional
    public void testFewRoutesCanHaveSameBusStop() {
        Route route = routeRepository.findAll().get(1);

        Route newRoute = new Route(40);
        newRoute.addBusStop(route.getBusStops().iterator().next());
        routeRepository.save(newRoute);

        Route savedRoute = routeRepository.findById(newRoute.getId()).get();

        assertTrue(savedRoute.getBusStops().contains(
                route.getBusStops().iterator().next()
        ));
    }

    public Employee saveEmployee(Employee newEmployee) {
        //TODO дублирование кода. есть проверка Employee.isCanBeDriver.
        // думаю нужно либо сделать отдельный метод сохранения водителя, либо отдельный метод валидации по должности
        boolean driverWithoutLicense =
                newEmployee.getPost().equals(Employee.Post.DRIVER) && newEmployee.getDriverLicense() == null;
        if (driverWithoutLicense) {
            throw new EmployeeException("Cannot save driver without driver license!");
        }

        return employeeRepository.save(newEmployee);
    }

    @Transactional
    public void testOneBusCanHaveFewMechanics() {
        Bus bus = busRepository.findAll().get(0);
        assertTrue(bus.getMechanics().size() > 1);
    }

    public void testOneMechanicCanWorkOnFewBuses() {
        Employee mechanic = employeeRepository.findFirstByPost(Employee.Post.MECHANIC);
        assertTrue(busRepository.findBusesByMechanicId(mechanic.getId()).size() > 1);
    }
}
