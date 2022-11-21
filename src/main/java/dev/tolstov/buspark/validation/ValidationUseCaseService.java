package dev.tolstov.buspark.validation;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.validation.use_cases.OnBusStopAddressSave;
import dev.tolstov.buspark.validation.use_cases.OnDriverSave;
import dev.tolstov.buspark.validation.use_cases.OnEmployeeAddressSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.HashSet;
import java.util.Set;

//todo wdzfxs(twitch) посоветовал использовать паттерн  chain of responsibility для формирования списка валидаций

@Service
@Validated
public class ValidationUseCaseService {

    @Autowired
    Validator validator;


    public void employeeAddressValidation(Address address) {
        Set<ConstraintViolation<Address>> useCaseViolations = validator.validate(address, OnEmployeeAddressSave.class);
        Set<ConstraintViolation<Address>> mainViolations = validator.validate(address);
        Set<ConstraintViolation<Address>> violations = new HashSet<>();
        violations.addAll(mainViolations);
        violations.addAll(useCaseViolations);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void busStopAddressValidation(Address address) {
        Set<ConstraintViolation<Address>> useCaseViolations = validator.validate(address, OnBusStopAddressSave.class);
        Set<ConstraintViolation<Address>> mainViolations = validator.validate(address);
        Set<ConstraintViolation<Address>> violations = new HashSet<>();
        violations.addAll(mainViolations);
        violations.addAll(useCaseViolations);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public void driverValidation(Employee employee) {
        Set<ConstraintViolation<Employee>> useCaseViolations = validator.validate(employee, OnDriverSave.class);
        Set<ConstraintViolation<Employee>> mainViolations = validator.validate(employee);
        Set<ConstraintViolation<Employee>> violations = new HashSet<>();
        violations.addAll(mainViolations);
        violations.addAll(useCaseViolations);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }


}
