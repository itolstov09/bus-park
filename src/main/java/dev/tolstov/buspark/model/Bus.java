package dev.tolstov.buspark.model;

import dev.tolstov.buspark.exception.EmployeeException;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "bus",
uniqueConstraints = {
        @UniqueConstraint(name = "bus_number_plate_key", columnNames = {"number_plate"}),
        @UniqueConstraint(name = "bus_driver_id_key", columnNames = {"driver_id"})
})
public class Bus {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Getter @Setter
    @Column(nullable = false)
    private String model;

    @NonNull
    @Getter @Setter
    @Column(name = "number_plate", nullable = false)
    private String numberPlate;

    @Getter @Setter
    @Column(name = "max_passenger")
    private Integer maxPassenger;

    @Getter
    @OneToOne
    @JoinColumn(
            name = "driver_id",
            foreignKey = @ForeignKey(name = "bus_driver_id_fkey") )
    private Employee driver;

    @Getter @Setter
    @OneToOne
    @JoinColumn(
            name = "route_id",
            foreignKey = @ForeignKey(name = "bus_route_id_fkey"))
    private Route route;

    @Getter @Setter
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
