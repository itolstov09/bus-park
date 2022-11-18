package dev.tolstov.buspark.model;

import dev.tolstov.buspark.validation.use_cases.OnDriverSave;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@RequiredArgsConstructor
@Getter @Setter
@Embeddable
public class DriverLicense {
    @NonNull
    @Column(name = "license_id")
    @NotBlank(groups = OnDriverSave.class)
    @NotBlank
    private String licenseID;

    @NotBlank(groups = OnDriverSave.class)
    @NotBlank
    //TODO переделать setter. сделать возможность передавать список категорий, который потом будет переводится в стрoку
    // todo валидация через @Pattern
    @NonNull
    @Column(name = "license_categories")
    private String licenseCategories;
}
