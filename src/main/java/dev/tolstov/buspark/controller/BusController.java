package dev.tolstov.buspark.controller;

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
    public List<Bus> findAll() {
        return busService.findAll();
    }

    @GetMapping("/{id}")
    public Bus findById(@PathVariable Long id) {
        return busService.findById(id);
    }

    @PostMapping
    public Bus save(@RequestBody Bus newBus) {
        return busService.save(newBus);
    }

    @PutMapping("/{id}")
    public Bus update(@PathVariable Long id, @RequestBody Bus busInfo) {
        return busService.update(id, busInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        busService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
