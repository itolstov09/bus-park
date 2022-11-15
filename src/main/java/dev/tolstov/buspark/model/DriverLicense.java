package dev.tolstov.buspark.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
@Embeddable
public class DriverLicense {
    @NonNull
    @Column(name = "license_id")
    private String licenseID;
    //TODO переделать setter. сделать возможность передавать список категорий, который потом будет переводится в стрoку
    @NonNull
    @Column(name = "license_categories")
    private String licenseCategories;
}
