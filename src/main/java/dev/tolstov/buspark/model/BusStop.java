package dev.tolstov.buspark.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(
        name = "bus_stop",
        uniqueConstraints = { @UniqueConstraint(
                                    name = "bus_stop_address_id_key",
                                    columnNames = {"address_id"})
        }
)
@NoArgsConstructor
@RequiredArgsConstructor
public class BusStop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Getter @Setter
    @Column(nullable = false)
    private String name;

    @NonNull
    @Getter @Setter
    @OneToOne //(cascade = CascadeType.PERSIST)
    @JoinColumn(
            name = "address_id",
            foreignKey = @ForeignKey(name = "bus_stop_address_id_fkey")
    )
    private Address address;
}
