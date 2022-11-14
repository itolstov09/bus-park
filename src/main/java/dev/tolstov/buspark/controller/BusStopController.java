package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.service.BusStopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("busStop")
public class BusStopController {

    @Autowired
    BusStopService busStopService;

    @GetMapping
    public List<BusStop> findAll() {
        return busStopService.findAll();
    }

    @GetMapping("/{id}")
    public BusStop findById(@PathVariable Long id) {
        return busStopService.findById(id);
    }

    @PostMapping
    public BusStop save(@RequestBody BusStop busstop) {
        Address busStopAddress = busstop.getAddress();
        return busStopService.save(busstop, busStopAddress);
    }

    @PutMapping("/{id}")
    public BusStop update(@PathVariable Long id, @RequestBody BusStop busStop) {
        return busStopService.update(id, busStop);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        busStopService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
