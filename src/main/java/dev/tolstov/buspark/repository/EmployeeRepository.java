package dev.tolstov.buspark.repository;

import dev.tolstov.buspark.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findFirstByPost(Employee.Post post);

    boolean existsByDriverLicenseLicenseID(String licenseID);
}
