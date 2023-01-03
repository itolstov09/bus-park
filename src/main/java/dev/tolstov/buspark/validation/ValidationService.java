package dev.tolstov.buspark.validation;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.model.Employee;
import dev.tolstov.buspark.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
public class ValidationService {
    private final Validator validator;

    private final ValidationUseCaseService validationUseCaseService;

    @Autowired
    public ValidationService(Validator validator, ValidationUseCaseService validationUseCaseService) {
        this.validator = validator;
        this.validationUseCaseService = validationUseCaseService;
    }


    public void addressValidation(Address newAddress) {
        Set<ConstraintViolation<Address>> constraintViolations = validator.validate(newAddress);
        if (!constraintViolations.isEmpty()) {
            throw new ConstraintViolationException(constraintViolations);
        }
    }

    public void busStopValidation(BusStop busStop) {
        Set<ConstraintViolation<BusStop>> violations = validator.validate(busStop);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        validationUseCaseService.busStopAddressValidation(busStop.getAddress());
    }

    public void routeValidation(Route route) {
        Set<ConstraintViolation<Route>> violations = validator.validate(route);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        route.getBusStops().forEach(this::busStopValidation);
    }

    public void employeeValidation(Employee employee) {
        Set<ConstraintViolation<Employee>> employeeViolations = validator.validate(employee);
        if (!employeeViolations.isEmpty()) {
            throw new ConstraintViolationException(employeeViolations);
        }
        validationUseCaseService.employeeAddressValidation(employee.getHomeAddress());
    }
}
