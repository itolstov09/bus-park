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
    @Query( value = "UPDATE employee " +
            "SET name=?1, last_name=?2, middle_name=?3 " +
            "WHERE id=?4", nativeQuery = true )
    Integer updateFIO(String name, String lastName, String middleName, Long id);


    @Modifying
    @Query("update Employee e " +
            "SET e.driverLicense=?1 " +
            "WHERE e.id=?2")
    Integer editLicense(DriverLicense license, Long id);

    @Modifying
    @Query(value = "update employee " +
            "SET post=?1 " +
            "WHERE id=?2", nativeQuery = true)
    void editPost(String newPost, Long id);

    @Modifying
    @Query(value = "DELETE FROM bus_mechanic WHERE mechanic_id=?1",
            nativeQuery = true)
    void removeBuses(Long id);

    long countByHomeAddress(Address address);
}
