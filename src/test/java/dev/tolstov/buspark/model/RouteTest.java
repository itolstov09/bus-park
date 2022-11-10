package dev.tolstov.buspark.model;

import org.junit.jupiter.api.Test;

public class RouteTest extends EntityTest {


    @Test
    void testFewRoutesCanHaveSameBusStop() {
        testEntityService.testFewRoutesCanHaveSameBusStop();
    }
}
