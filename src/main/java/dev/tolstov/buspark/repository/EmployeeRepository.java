package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findFirstByPost(Employee.Post post);

    boolean existsByDriverLicenseLicenseID(String licenseID);


    @Query(value = "SELECT e.id " +
            "FROM employee e " +
            "WHERE e.license_id=?",

            nativeQuery = true)
    Long getIdByLicenseID(String licenseID);
}
