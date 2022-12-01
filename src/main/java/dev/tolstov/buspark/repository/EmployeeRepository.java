package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.DriverLicense;
import dev.tolstov.buspark.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.Min;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findFirstByPost(Employee.Post post);

    boolean existsByDriverLicenseLicenseID(String licenseID);


    @Query(value = "SELECT e.id " +
            "FROM employee e " +
            "WHERE e.license_id=?",

            nativeQuery = true)
    Long getIdByLicenseID(String licenseID);

    List<Employee> findByLastName(String lastName);

    List<Employee> findByName(String name);

    List<Employee> findByMiddleName(String middleName);

    @Modifying
    @Query( "UPDATE Employee e " +
            "SET e.name=?1, e.lastName=?2, e.middleName=?3 " +
            "WHERE e.id=?4" )
    Integer updateFIO(String name, String lastName, String middleName, Long id);


    @Modifying
    @Query("update Employee e " +
            "SET e.driverLicense=?1 " +
            "WHERE e.id=?2")
    Integer editLicense(DriverLicense license, Long id);
}
