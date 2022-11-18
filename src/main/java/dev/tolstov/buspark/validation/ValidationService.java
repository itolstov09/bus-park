package dev.tolstov.buspark.validation;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class ValidationService {
    @Autowired
    Validator validator;


    private void addressValidation(Address address) {
        Set<ConstraintViolation<Address>> violations = validator.validate(address);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }




    public void busStopValidation(BusStop busStop) {
        // TODO после того как сделаю валидацию для всех сервисов. Стоит придумать способ по лучше
        Map<String, String> busStopAddressViolations = getBusStopAddressViolations(busStop.getAddress());
        String addressViolations;
        if (!busStopAddressViolations.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            busStopAddressViolations.forEach((key, value) -> {
                stringBuilder.append(key);
                stringBuilder.append(":");
                stringBuilder.append(value);
                stringBuilder.append("\n");
            });
            addressViolations = "Can't save bus stop with address:\n" + stringBuilder;
            throw new ConstraintViolationException(addressViolations, null);
        }

        Set<ConstraintViolation<BusStop>> violations = validator.validate(busStop);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private Map<String, String> getBusStopAddressViolations(Address busStopAddress) {
        boolean isCanBeBusStopAddress = busStopAddress.getApartmentNumber() == null;
        Map<String, String> err = new HashMap<>();
        if (!isCanBeBusStopAddress) {
            err.put("apartment_number", "apartment number must be null");
            err.put("test", "aga");
        }
        return err;
    }

}
