package dev.tolstov.buspark.model;

import dev.tolstov.buspark.exception.EmployeeException;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "bus",
uniqueConstraints = {
        @UniqueConstraint(name = "bus_number_plate_key", columnNames = {"number_plate"}),
        @UniqueConstraint(name = "bus_driver_id_key", columnNames = {"driver_id"})
})
@NoArgsConstructor
@RequiredArgsConstructor
@Setter @Getter
public class Bus extends BPEntity {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NonNull
    @Column(nullable = false)
    private String model;

    @NotNull
    @Pattern(regexp = "[0-9]{3} [A-Z]{2,3} [0-9]{2}", message = "Номер не соответствует формату РК")
    @NonNull
    @Column(name = "number_plate", nullable = false)
    private String numberPlate;

    @NotNull
    @Min(20)
    @Max(60)
    @NonNull
    @Column(name = "max_passenger")
    private Integer maxPassenger;

    @OneToOne
    @JoinColumn(
            name = "driver_id",
            foreignKey = @ForeignKey(name = "bus_driver_id_fkey") )
    private Employee driver;

    @OneToOne
    @JoinColumn(
            name = "route_id",
            foreignKey = @ForeignKey(name = "bus_route_id_fkey"))
    private Route route;

    @ManyToMany
    @JoinTable(
            name = "bus_mechanic",
            joinColumns = { @JoinColumn(
                    name = "bus_id",
                    foreignKey = @ForeignKey(name = "bus_mechanic_bus_id_fkey" ) )
            },
            inverseJoinColumns = { @JoinColumn(
                    name = "mechanic_id",
                    foreignKey = @ForeignKey(name = "bus_mechanic_mechanic_id_fkey") )
            }
    )
    private Set<Employee> mechanics = new HashSet<>();

    //TODO скорее всего надо добавить проверку на то, не водит ли сотрудник
    // другой автобус. Сейчас это происходит только при сохранении
    public void setDriver(Employee employee) {
        if (!employee.isCanBeDriver()) {
            throw new EmployeeException(
                    String.format(
                            "Employee %s can not be a driver! Reason: don't have a license!",
                            employee.getName() + employee.getLastName()
                    )
            );
        }
        // TODO добавить удаление обслуживаемых автобусов, так как сотрудник больше не является механиком
        employee.setPost(Employee.Post.DRIVER);
        this.driver = employee;
    }

    public void addMechanic(Employee mechanic) {
        getMechanics().add(mechanic);
    }

}
