package dev.tolstov.buspark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BPEntityNotFoundException extends BusParkBaseException {
    public BPEntityNotFoundException(String entityName, Long id) {
        super(String.format("Cannot find %s entity with id: %d", entityName, id));
    }
}
