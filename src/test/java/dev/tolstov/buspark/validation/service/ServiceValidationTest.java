package dev.tolstov.buspark.validation.service;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.validation.ValidationUseCaseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class ServiceValidationTest {

    @Autowired
    ValidationUseCaseService validationUseCaseService;


    @Test
    void whenValidateValidEmployeeAddress_thenDontThrowException() {
        Address employeeAddress = new Address("Street", 15);
        assertDoesNotThrow(() -> validationUseCaseService.employeeAddressValidation(employeeAddress));
    }

    @Test
    void whenValidateValidBusStopAddress_thenDontThrowException() {
        Address busStopAddress = new Address("Street", null);
        assertDoesNotThrow(() -> validationUseCaseService.busStopAddressValidation(busStopAddress));
    }


}
