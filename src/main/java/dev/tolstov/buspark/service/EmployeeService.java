package dev.tolstov.buspark.service;

import dev.tolstov.buspark.dto.EmployeeDriverDTO;
import dev.tolstov.buspark.dto.EmployeeMechanicDTO;
import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.EmployeeRepository;
import dev.tolstov.buspark.validation.ValidationService;
import dev.tolstov.buspark.validation.ValidationUseCaseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Service
@Validated
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AddressService addressService;

    @Autowired
    ValidationService validationService;

    @Autowired
    ValidationUseCaseService validationUseCaseService;



    //TODO убрать. используется только в тестах
    public Employee save(Employee employee, Address homeAddress) {
        employee.setHomeAddress(homeAddress);
        return save(employee);
    }

    // todo задать вопрос в ТГ чате, как вынести дублированный код в отдельный метод не городя перегрузку
    @Validated
    public Employee createMechanic(@Valid EmployeeMechanicDTO mechanic) {
        return save(mapMechanicDTOtoEmployee(mechanic));
    }

    @Validated
    public Employee createDriver(@Valid EmployeeDriverDTO driver) {
        return save(mapDriverDTOToEmployee(driver));
    }


    // используется в тестах
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Employee findById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElseThrow(
                () -> new BPEntityNotFoundException(
                        Employee.class.getSimpleName(), employeeId)
        );
    }

    @Validated
    public Employee updateDriver(Long id, @Valid EmployeeDriverDTO driver) {
        Employee byId = findById(id);
        if (byId.getPost() != Employee.Post.DRIVER) {
            throw new EmployeeException("Trying put driver info in mechanic entity");
        }
        BeanUtils.copyProperties(driver, byId);
        return save(byId);
    }

    @Validated
    public Employee updateMechanic(Long id, @Valid EmployeeMechanicDTO mechanic) {
        Employee byId = findById(id);
        if (byId.getPost() != Employee.Post.MECHANIC) {
            throw new EmployeeException("Trying put mechanic info in driver entity");
        }
        BeanUtils.copyProperties(mechanic, byId);
        return save(byId);
    }

    public void deleteById(Long employeeId) {
        Employee byId = findById(employeeId);
        employeeRepository.delete(byId);
    }

    public void deleteAll() {
        employeeRepository.deleteAll();
    }

    public Employee findFirstByPost(Employee.Post post) {
        return employeeRepository.findFirstByPost(post);
    }

    private Employee save(Employee employee) {

        DriverLicense license = employee.getDriverLicense();
        //todo как по мне тут потенциальный баг. Если обновлять данные одного водителя и
        // подставить лицензию другого, то выдаст 500. Поскольку проверка по ID лицензии скорее всего не произойдет

        Long employeeId = employee.getId();
        String licenseID = license.getLicenseID();
        Long idByLicenseID = employeeRepository.getIdByLicenseID(licenseID);
        if ( employeeId == null && employeeRepository.existsByDriverLicenseLicenseID(licenseID)
                || idByLicenseID != null && !Objects.equals(idByLicenseID, employeeId)
        ) {
            throw new EntityExistsException("Cannot save employee with exist driver license");
        }

        addressService.save(employee.getHomeAddress());
        return employeeRepository.save(employee);
    }

    private Employee mapMechanicDTOtoEmployee(EmployeeMechanicDTO mechanic) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(mechanic, employee);
        Employee.Post post = Employee.Post.valueOf(mechanic.getPost());
        employee.setPost(post);
        return employee;
    }

    private Employee mapDriverDTOToEmployee(EmployeeDriverDTO driver) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(driver, employee);
        Employee.Post post = Employee.Post.valueOf(driver.getPost());
        employee.setPost(post);
        return employee;
    }

    public Page<Employee> getPage(Integer page, Integer size) {
        return employeeRepository.findAll(PageRequest.of(page, size));
    }
}

