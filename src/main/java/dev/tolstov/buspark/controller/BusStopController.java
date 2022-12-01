package dev.tolstov.buspark.controller;

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

    @GetMapping("find-byName")
    public ResponseEntity<BusStop> findByName(@RequestParam String name) {
        return ResponseEntity.ok(busStopService.findByName(name));
    }

    @GetMapping("find-byStreet")
    public ResponseEntity<List<BusStop>> findByStreet(@RequestParam String street) {
        List<BusStop> busStopList = busStopService.findByStreet(street);
        if (busStopList == null || busStopList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(busStopList);
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

    //todo слишком запутанно получилось. тут либо нужен join либо еще чтото чтобы сократить до одного запроса к БД
    @PatchMapping("/{id}/editAddressStreet")
    public ResponseEntity<String> updateAddress( @PathVariable Long id, @RequestParam String street ) {
        return ResponseEntity.ok(String.format("Rows updated: %d", busStopService.updateAddressStreet(street, id)));
    }

    @PatchMapping("/{id}/editName")
    public ResponseEntity<String> editName(@PathVariable Long id, @RequestParam String name) {
        return ResponseEntity.ok(String.format("Rows updated: %d", busStopService.editName(name, id)));
    }

    @PatchMapping("/{id}/setAddress")
    public ResponseEntity<String> setAddress(@PathVariable(name = "id") Long busStopId, @RequestParam Long addressId) {
        return ResponseEntity.ok(String.format("Rows updated: %d", busStopService.setAddress(addressId, busStopId)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable Long id) {
        busStopService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

//    @DeleteMapping
//    public ResponseEntity<HttpStatus> deleteByName(@RequestParam String name) {
//        busStopService.deleteByName(name);
//        return ResponseEntity.noContent().build();
//    }
}
