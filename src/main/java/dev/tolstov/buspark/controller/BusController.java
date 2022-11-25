package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.dto.BusDTO;
import dev.tolstov.buspark.model.Bus;
import dev.tolstov.buspark.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<Bus>> findAll() {
        List<Bus> busList = busService.findAll();
        if (busList == null || busList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(busList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Bus> findById(@PathVariable Long id) {
        return ResponseEntity.ok(busService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Bus> save(@RequestBody Bus newBus) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(busService.save(newBus));
    }

    @PostMapping("/short")
    public ResponseEntity<Bus> save( @RequestBody BusDTO dto ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(busService.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Bus> update(@PathVariable Long id, @RequestBody Bus busInfo) {
        return ResponseEntity.ok(busService.update(id, busInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        busService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
