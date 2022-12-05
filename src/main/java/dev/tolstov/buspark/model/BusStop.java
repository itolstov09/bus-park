package dev.tolstov.buspark.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name = "bus_stop",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "bus_stop_name_key",
                        columnNames = {"name"})
        }
)
@NoArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
public class BusStop extends BPEntity {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @NotNull
    @OneToOne
    @JoinColumn(
            name = "address_id",
            foreignKey = @ForeignKey(name = "bus_stop_address_id_fkey"),
            nullable = false
    )
    private Address address;
}
