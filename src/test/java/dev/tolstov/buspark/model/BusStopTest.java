package dev.tolstov.buspark.model;

import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BusStopTest extends EntityTest {


    @Test
    void testAddExistingBusStopAddressToNewBusStopThrowsException() {
        BusStop busStop = busStopService.findAll().get(0);
        Address busStopAddress = busStop.getAddress();

        BusStop anotherBusStop = new BusStop("Another bus stop");
        DataIntegrityViolationException violationException = assertThrows(
                DataIntegrityViolationException.class,
                () -> busStopService.save(anotherBusStop, busStopAddress)
        );
        assertTrue(
                Objects.requireNonNull(violationException.getRootCause())
                        .getMessage().contains("bus_stop_address_id_key") );
    }
}
