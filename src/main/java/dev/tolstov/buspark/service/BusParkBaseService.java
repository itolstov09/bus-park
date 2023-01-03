package dev.tolstov.buspark.service;

import dev.tolstov.buspark.model.BPEntity;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.util.List;

@Validated
public interface BusParkBaseService<T extends BPEntity> {
    List<T> findAll();

    T findById(Long id);

    Page<T> getPage(@Min(0) Integer page, @Positive Integer size);

    void deleteAll();

    void deleteById(Long id);
}
