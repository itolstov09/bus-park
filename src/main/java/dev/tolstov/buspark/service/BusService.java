package dev.tolstov.buspark.service;

import dev.tolstov.buspark.dto.BusDTO;
import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.exception.BusStopException;
import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.exception.RouteException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.repository.BusRepository;
import dev.tolstov.buspark.repository.RouteRepository;
import dev.tolstov.buspark.validation.ValidationUseCaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Validated
public class BusService {
    @Autowired
    BusRepository busRepository;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    RouteRepository routeRepository;

    @Autowired
    ValidationUseCaseService validationUseCaseService;


    @Validated
    public Bus create(@Valid BusDTO bus) {
        Bus entity = new Bus();
        BeanUtils.copyProperties(bus, entity);
        return save(entity);
    }

    private Bus save(Bus bus) {
        Long busId = bus.getId();
        String busNumberPlate = bus.getNumberPlate();
        Long idByNumberPlate = busRepository.getIdByNumberPlate(busNumberPlate);

        if ( busId == null && busRepository.existsByNumberPlate(busNumberPlate)
                || idByNumberPlate != null && !Objects.equals(idByNumberPlate, busId)
        ) {
            throw new EntityExistsException(
                    String.format("Bus with number plate '%s' already exists!",
                            busNumberPlate));
        }

        // валидация перед сохранением не нужна, поскольку дто уже валидировалось
        return busRepository.save(bus);
    }



    public List<Bus> findAll() {
        return busRepository.findAll();
    }

    public Bus findById(Long busId) {
        return busRepository.findById(busId)
                .orElseThrow(
                        () -> new BPEntityNotFoundException(
                                Bus.class.getSimpleName(), busId)
                );
    }

    @Validated
    public Bus update(Long id, @Valid BusDTO dto) {
        Bus byId = busRepository.findById(id).orElseThrow(() ->
                new BPEntityNotFoundException(Bus.class.getSimpleName(), id));
        byId.setModel(dto.getModel());
        byId.setNumberPlate(dto.getNumberPlate());
        byId.setMaxPassenger(dto.getMaxPassenger());

        return save(byId);
    }

    public void deleteById(Long busId) {
        Bus bus = findById(busId);
        busRepository.delete(bus);
    }

    public void deleteAll() {
        busRepository.deleteAll();
    }

    public List<Bus> findBusesByMechanicId(Long mechanicId) {
        return busRepository.findBusesByMechanicId(mechanicId);
    }


    public void setDriver(Bus bus, Employee employee) {
        driverVerification(employee);
        bus.setDriver(employee);
    }


    // TODO перевести в boolean когда потребуется проверка должности механика
    // TODO вынести в другой метод: Добавить удаление обслуживаемых автобусов, так как сотрудник больше не является механиком
    private void driverVerification(Employee employee) {
        String lastName = employee.getLastName();
        String name = employee.getName();

        if (!employee.isCanBeDriver()) {
            throw new EmployeeException(
                    String.format(
                            "Employee %s can not be a driver! Reason: don't have a license!",
                            name + lastName )
            );
        }

    }

    public Bus save(Bus newBus, Employee driver) {
        setDriver(newBus, driver);
        return save(newBus);
    }

    public Bus save(Bus newBus, Employee driver, Set<Employee> mechanics) {
        setDriver(newBus, driver);
        newBus.setMechanics(mechanics);
        return save(newBus);
    }


    public Page<Bus> getPage(Integer page, Integer size) {
        return busRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public void addMechanic(@Positive Long mechanicId, @Positive Long busId) {
        checkExistById(busId);
        if (!employeeService.existById(mechanicId)) {
            throw new BusStopException(
                    String.format("Cannot add mechanic. Mechanic with id %d not found", mechanicId));
        }

        //todo костыль. тут либо надо в sql запросе проверять уникальность. или еще как то
        try {
            busRepository.addMechanic(busId, mechanicId);
        } catch (DataIntegrityViolationException exception) {
            if (exception.getRootCause().getMessage().contains("bus_mechanic_pkey")) {
                throw new EntityExistsException("This bus already have mechanic with id " + mechanicId);
            }
        }
    }

    @Transactional
    public void removeMechanic(Long mechanicId, Long busId) {
        checkExistById(busId);
        busRepository.removeMechanic(busId, mechanicId);
    }

    @Transactional
    public void setBusDriver(Long driverId, Long busId) {
        Employee driver = employeeService.findById(driverId);
        validationUseCaseService.driverValidation(driver);

        String name = driver.getName();
        String lastName = driver.getLastName();
        String anotherBusNumberPlate = busRepository.numberPlateByDriverNameAndLastName(name, lastName);
        if (anotherBusNumberPlate != null) {
            throw new EmployeeException(String.format("Driver '%s %s' already drives bus '%s'",
                    name, lastName, anotherBusNumberPlate));
        }

        busRepository.setBusDriver(driverId, busId);
    }

    @Transactional
    public void setRoute(Long routeId, Long busId) {
        checkExistById(busId);
        if (!routeRepository.existsById(routeId)) {
            throw new RouteException(
                    String.format("Cannot set route to bus: Route with id %d not found!", routeId) );
        }

        busRepository.setRoute(routeId, busId);
    }

    private void checkExistById(Long id) {
        if (!busRepository.existsById(id)) {
            throw new BPEntityNotFoundException(Bus.class.getSimpleName(), id);
        }
    }

}
