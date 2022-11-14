package dev.tolstov.buspark.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "bus_stop",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "bus_stop_address_id_key",
                        columnNames = {"address_id"}),
                @UniqueConstraint(
                        name = "bus_stop_name_key",
                        columnNames = {"name"})
        }
)
@NoArgsConstructor
@RequiredArgsConstructor
public class BusStop {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Getter @Setter
    @Column(nullable = false)
    private String name;

    @Getter @Setter
    @OneToOne
    @JoinColumn(
            name = "address_id",
            foreignKey = @ForeignKey(name = "bus_stop_address_id_fkey"),
            nullable = false
    )
    private Address address;
}
