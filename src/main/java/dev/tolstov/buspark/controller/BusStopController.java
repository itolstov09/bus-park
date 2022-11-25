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
@RequestMapping("api/v1/busStops")
public class BusStopController {

    @Autowired
    BusStopService busStopService;

    @GetMapping
    public ResponseEntity<?> findAll() {
        List<BusStop> busStopList = busStopService.findAll();
        if (busStopList == null || busStopList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(busStopList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusStop> findById(@PathVariable Long id) {
        return ResponseEntity.ok(busStopService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
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
