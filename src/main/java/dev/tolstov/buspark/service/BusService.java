package dev.tolstov.buspark.service;

import dev.tolstov.buspark.dto.BusDTO;
import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.BusRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@Service
@Validated
public class BusService {
    @Autowired
    BusRepository busRepository;



    @Validated
    public Bus create(@Valid BusDTO bus) {
        Bus entity = new Bus();
        BeanUtils.copyProperties(bus, entity);
        return save(entity);
    }

    private Bus save(Bus bus) {
        if (bus.getId() == null && busRepository.existsByNumberPlate(bus.getNumberPlate())) {
            throw new EntityExistsException(
                    String.format("Bus with number plate '%s' already exists!",
                            bus.getNumberPlate()));
        }

        //TODO перенести в метод когда дойду до CRUD
//        Employee driver = bus.getDriver();
//        String name = driver.getName();
//        String lastName = driver.getLastName();
//        String anotherBusNumberPlate = busRepository.numberPlateByDriverNameAndLastName(name, lastName);
//        if (anotherBusNumberPlate != null) {
//            throw new EmployeeException(String.format("Driver '%s %s' already drives bus '%s'",
//                    name, lastName, anotherBusNumberPlate));
//        }

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



}
