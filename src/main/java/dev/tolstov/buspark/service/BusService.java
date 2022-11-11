package dev.tolstov.buspark.service;

import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class BusService {
    @Autowired
    BusRepository busRepository;


    public Bus save(Bus bus) {
        return busRepository.save(bus);
    }

    public Bus save(Bus newBus, Employee driver) {
        setDriver(newBus, driver);
        return busRepository.save(newBus);
    }

    public Bus save(Bus newBus, Employee driver, Set<Employee> mechanics) {
        setDriver(newBus, driver);
        newBus.setMechanics(mechanics);
        return busRepository.save(newBus);
    }


    public List<Bus> findAll() {
        return busRepository.findAll();
    }

    public Bus findById(Long busId) {
        return busRepository.findById(busId)
                .orElseThrow(
                        () -> new BPEntityNotFoundException(String.format("Bus with id \"%s\" not found!", busId))
                );
    }

    public Bus update(Bus busInfo) {
        // TODO возможно стоит сделать проверку id != null, ведь если это так, то это не обновление, а создание
        return busRepository.save(busInfo);
    }

    public void deleteById(Long busId) {
        busRepository.deleteById(busId);
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
        if (!employee.isCanBeDriver()) {
            throw new EmployeeException(
                    String.format(
                            "Employee %s can not be a driver! Reason: don't have a license!",
                            employee.getName() + employee.getLastName()
                    )
            );
        }

        Bus busByDriver = busRepository.findBusByDriver(employee);
        if (busByDriver != null) {
            throw new EmployeeException(String.format("Driver already drives bus %s", busByDriver.getNumberPlate()));
        }
    }
}
