package dev.tolstov.buspark.service;

import dev.tolstov.buspark.dto.EmployeeDriverDTO;
import dev.tolstov.buspark.dto.EmployeeFIODTO;
import dev.tolstov.buspark.dto.EmployeeMechanicDTO;
import dev.tolstov.buspark.exception.BPEntityNotFoundException;
import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.repository.EmployeeRepository;
import dev.tolstov.buspark.validation.ValidationService;
import dev.tolstov.buspark.validation.ValidationUseCaseService;
import dev.tolstov.buspark.validation.constraints.ValueOfEnum;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.persistence.EntityExistsException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
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
        if (license != null) {
            Long employeeId = employee.getId();
            String licenseID = license.getLicenseID();
            Long idByLicenseID = employeeRepository.getIdByLicenseID(licenseID);
            if ( employeeId == null && employeeRepository.existsByDriverLicenseLicenseID(licenseID)
                    || idByLicenseID != null && !Objects.equals(idByLicenseID, employeeId)
            ) {
                throw new EntityExistsException("Cannot save employee with exist driver license");
            }
        }
        Address homeAddress = employee.getHomeAddress();
        validationUseCaseService.employeeAddressValidation(homeAddress);

        addressService.save(homeAddress);
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

    public Page<Employee> getPage(@Min(0) Integer page, @Positive Integer size) {
        return employeeRepository.findAll(PageRequest.of(page, size));
    }

    public List<Employee> findByLastName(@NotBlank String lastName) {
        return employeeRepository.findByLastName(lastName);
    }

    public List<Employee> findByName(@NotBlank String name) {
        return employeeRepository.findByName(name);
    }

    public List<Employee> findByMiddleName(String middleName) {
        return employeeRepository.findByMiddleName(middleName);
    }

    @Transactional
    @Validated
    public Integer updateFIO(@Valid EmployeeFIODTO dto, Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new BPEntityNotFoundException(
                    String.format("Cannot update FIO. Employee with id %d not found", id) );
        }
        return employeeRepository.updateFIO(dto.getName(), dto.getLastName(), dto.getMiddleName(), id);
    }


    public void editHomeAddress(Address homeAddress, Long id) {
        validationUseCaseService.employeeAddressValidation(homeAddress);

        if (!employeeRepository.existsById(id)) {
            throw new BPEntityNotFoundException(Employee.class.getSimpleName(), id);
        }

        addressService.save(homeAddress);
    }

    @Transactional
    public Integer editLicense(DriverLicense license, Long id) {
        validationUseCaseService.driverLicenseValidation(license);
        if (!existById(id)) {
            throw new BPEntityNotFoundException(Employee.class.getSimpleName(), id);
        }

        return employeeRepository.editLicense(license, id);
    }

    public boolean existById(Long id) {
        return employeeRepository.existsById(id);
    }

    @Transactional
    public void editPost(
            @ValueOfEnum(enumClass = Employee.Post.class) String newPost,
            @Positive Long id
    ) {
        Employee employee = findById(id);
        // проверка - указана ли та же должность. если да - 400
        String oldPost = employee.getPost().name();
        if (newPost.equals(oldPost)) {
            throw new EmployeeException("Employee already have post " + newPost);
        }

        // тут пока 2 варианта
        // с механика на водителя
        if (oldPost.equals(Employee.Post.MECHANIC.name()) && newPost.equals(Employee.Post.DRIVER.name())) {
            //      валидация как водителя
            validationUseCaseService.driverValidation(employee);
            //      у механика нужно убрать обслуживаемые автобусы
            employeeRepository.removeBuses(id);
        }

        //todo если водитель становится механиком, то можно сказать что он механик у своего же автобуса.
        // или нужно жестко разделять должности и ответственность. либо механик либо водитель

        // с водителя на механика - пока ничего проверять не нужно.. или нужно?

        employeeRepository.editPost(newPost, id);
    }
}

