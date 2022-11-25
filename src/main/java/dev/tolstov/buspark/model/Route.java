package dev.tolstov.buspark.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.LinkedHashSet;
import java.util.Set;

@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "route_route_number_key", columnNames = {"route_number"})
        }
)
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Positive
    @NotNull
    @NonNull
    @Column(name = "route_number", nullable = false)
    private Integer routeNumber;

    @NotEmpty(message = "Список остановок у маршрута не должен быть пустым")
    @ManyToMany
    @JoinTable(
            name = "route_bus_stop",
            joinColumns = {
                    @JoinColumn(
                            name = "route_id",
                            foreignKey = @ForeignKey(name = "route_bus_stop_route_id_fkey")
                    )
            },
            inverseJoinColumns = {
                    @JoinColumn(
                            name = "bus_stop_id",
                            foreignKey = @ForeignKey(name = "route_bus_stop_bus_stop_id_fkey")
                    )
            }
    )
    private Set<BusStop> busStops = new LinkedHashSet<>();

    public void addBusStop(BusStop busStop) {
        getBusStops().add(busStop);
    }

}
