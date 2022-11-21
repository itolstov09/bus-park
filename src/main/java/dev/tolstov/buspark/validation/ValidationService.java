package dev.tolstov.buspark.validation;

import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;

@Service
public class ValidationService {
    @Autowired
    Validator validator;

    @Autowired
    ValidationUseCaseService validationUseCaseService;


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
}
