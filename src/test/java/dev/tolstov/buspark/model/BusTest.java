package dev.tolstov.buspark.model;

import dev.tolstov.buspark.dto.BusDTO;
import dev.tolstov.buspark.exception.EmployeeException;
import dev.tolstov.buspark.repository.BusRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityExistsException;
import javax.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

//@Disabled
public class BusTest extends EntityTest {
    @Autowired
    BusRepository busRepository;

    /**
     * попытка сохранить автобус с уже существующим номером вызовет исключение
     */
    @Test
    void whenSaveBusWithExistsNumberPlate_thenThrowsException() {
        assertThrows(EntityExistsException.class, () -> {
            Bus busFromDB = busServiceImpl.findAll().get(0);
            String numberPlate = busFromDB.getNumberPlate();
            Bus newBus = new Bus("model", numberPlate, 20);
            BusDTO busDTO = new BusDTO();
            BeanUtils.copyProperties(newBus, busDTO);
            busServiceImpl.create(busDTO); }
        );
    }


    //      * сотрудника без водительского удостоверения нельзя указать как водителя
    @Test
    void testAddEmployeeWithoutLicenseCannotBeBusDriver() {
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class,
                () -> {
                    Employee employee = employeeServiceImpl.findAll().get(0);
                    employee.setDriverLicense(null);
                    Bus bus = busServiceImpl.findAll().get(0);
                    busServiceImpl.setBusDriver(employee.getId(), bus.getId());
                });
        assertEquals("driverLicense: не должно равняться null", exception.getMessage());
    }

//
    @Test
    void whenSaveBusWithDriverWhosAlreadyDrivesBus_thenThrowsException() {
        EmployeeException exception = assertThrows(
                EmployeeException.class,
                () -> {
                    Bus bus = busServiceImpl.findAll().get(0);
                    Employee driver = bus.getDriver();

                    BusDTO busDTO = new BusDTO();
                    busDTO.setModel("model");
                    busDTO.setNumberPlate("585 TTT 01");
                    busDTO.setMaxPassenger(57);
                    Bus newBus = busServiceImpl.create(busDTO);

                    busServiceImpl.setBusDriver(driver.getId(), newBus.getId());
                }
        );
        System.out.println("exc -> " + exception.getMessage());
        assertTrue( exception.getMessage().contains("already drives bus"));
    }

    //      одним автобусом могут заниматься несколько механиков
    @Test
    void testOneBusCanHaveFewMechanics() {
        testEntityService.testOneBusCanHaveFewMechanics();
    }

    //    у одного механика может быть на обслуживании несколько автобусов
    @Test
    void testOneMechanicCanWorkOnFewBuses() {
        testEntityService.testOneMechanicCanWorkOnFewBuses();
    }

    @Test
    void givenBusWithDriver_whenSetDriver_thenDriverChanged() {
        //given
        Employee mechanic = employeeServiceImpl.findFirstByPost(Employee.Post.MECHANIC);
        DriverLicense driverLicense = new DriverLicense("123", "D");
        employeeServiceImpl.editLicense(driverLicense, mechanic.getId());

        Bus bus = busServiceImpl.findAll().get(0);
        //when
        Employee oldDriver = bus.getDriver();
        busServiceImpl.setBusDriver(mechanic.getId(), bus.getId());
        //then
        Bus busById = busServiceImpl.findById(bus.getId());
        assertNotEquals(busById.getDriver(), oldDriver);
    }
}
