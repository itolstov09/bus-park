package dev.tolstov.buspark.model;

import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@RequiredArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NonNull
    @Getter @Setter
    @Column(name = "route_number", nullable = false)
    private Integer routeNumber;

    @Getter @Setter
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
