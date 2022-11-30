package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.dto.BusDTO;
import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/buses")
public class BusController {

    @Autowired
    BusService busService;


    @GetMapping
    public ResponseEntity<Page<Bus>> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return ResponseEntity.ok(busService.getPage(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bus> findById(@PathVariable Long id) {
        return ResponseEntity.ok(busService.findById(id));
    }

    // сохраняем только DTO. Полную модель не трогай.
    @PostMapping
    public ResponseEntity<Bus> save( @RequestBody BusDTO dto ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(busService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bus> update(@PathVariable Long id, @RequestBody BusDTO dto) {
        return ResponseEntity.ok(busService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        busService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
