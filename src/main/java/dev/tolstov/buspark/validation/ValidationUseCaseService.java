package dev.tolstov.buspark.validation;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
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
import java.util.Set;

//todo wdzfxs(twitch) посоветовал использовать паттерн  chain of responsibility для формирования списка валидаций

@Service
@Validated
public class ValidationUseCaseService {

    @Autowired
    Validator validator;


    public void employeeAddressValidation(Address address) {
        Set<ConstraintViolation<Address>> useCaseViolations = validator
                .validate(address, OnEmployeeAddressSave.class);
        if (!useCaseViolations.isEmpty()) {
            throw new ConstraintViolationException(useCaseViolations);
        }
    }

    public void busStopAddressValidation(Address address) {
        Set<ConstraintViolation<Address>> useCaseViolations = validator.validate(address, OnBusStopAddressSave.class);
        if (!useCaseViolations.isEmpty()) {
            throw new ConstraintViolationException(useCaseViolations);
        }
    }

    public void driverLicenseValidation(DriverLicense driverLicense) {
        Set<ConstraintViolation<DriverLicense>> useCaseViolations = validator.validate(driverLicense, OnDriverSave.class);
        if (!useCaseViolations.isEmpty()) {
            throw new ConstraintViolationException(useCaseViolations);
        }
    }


    public void driverValidation(Employee employee) {
        Set<ConstraintViolation<Employee>> useCaseViolations = validator.validate(employee, OnDriverSave.class);
        if (!useCaseViolations.isEmpty()) {
            throw new ConstraintViolationException(useCaseViolations);
        }
        driverLicenseValidation(employee.getDriverLicense());
    }
}
