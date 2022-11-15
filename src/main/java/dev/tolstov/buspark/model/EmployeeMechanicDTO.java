package dev.tolstov.buspark.model;


import lombok.Data;

@Data
public class EmployeeMechanicDTO {
    private String name;
    private String lastName;
    private String middleName;
    private Double salary;
    private Address homeAddress;
    private String post;
}
