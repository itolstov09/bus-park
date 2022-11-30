package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.model.BusStop;
import dev.tolstov.buspark.service.BusStopService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<Page<BusStop>> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return ResponseEntity.ok(busStopService.getPage(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusStop> findById(@PathVariable Long id) {
        return ResponseEntity.ok(busStopService.findById(id));
    }

    @PostMapping
    public ResponseEntity<BusStop> save(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Caution: Bus stop address apartment number must be null")
            BusStop newBusStop
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(busStopService.save(newBusStop));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BusStop> update(
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Caution: Bus stop address apartment number must be null",
            content = @Content(examples = {
                    @ExampleObject(
                            name = "valid",
                            ref = "#/components/examples/bus-stop-PUT-200"),
                    @ExampleObject(
                            name = "invalid",
                            ref = "#/components/examples/bus-stop-PUT-400")
                    }
            ) )
            @RequestBody BusStop busStop) {
        return ResponseEntity.ok(busStopService.update(id, busStop));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        busStopService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
