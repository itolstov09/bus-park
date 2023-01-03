package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.service.RouteServiceImpl;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/routes")
public class RouteController {
    @Autowired
    RouteServiceImpl routeServiceImpl;


    @GetMapping
    public ResponseEntity<Page<Route>> getPage(
            @RequestParam Integer page,
            @RequestParam Integer size
    ) {
        return ResponseEntity.ok(routeServiceImpl.getPage(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> findById(@PathVariable Long id) {
        return ResponseEntity.ok(routeServiceImpl.findById(id));
    }

    @PostMapping
    public ResponseEntity<Route> save(

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(examples = {
                            @ExampleObject(
                                    name = "valid 1",
                                    ref = "#/components/examples/route-POST-201-ex1"),
                            @ExampleObject(
                                    name = "valid 2",
                                    ref = "#/components/examples/route-POST-201-ex2")
                    }
                    ) )
            @RequestBody Route route) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeServiceImpl.save(route));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> update(@PathVariable Long id, @RequestBody Route routeInfo) {
        return ResponseEntity.ok(routeServiceImpl.update(id, routeInfo));
    }

    @PatchMapping("/{id}/addBusStop")
    public ResponseEntity<Void> addBusStop(
            @PathVariable(name = "id") Long routeId,
            @RequestParam Long busStopId
    ) {
        routeServiceImpl.addBusStop(busStopId, routeId);
        return ResponseEntity.noContent().build();

    }

    @PatchMapping("/{id}/removeBusStop")
    public ResponseEntity<Void> removeBusStop(
            @PathVariable(name = "id") Long routeId,
            @RequestParam Long busStopId
    ) {
        routeServiceImpl.removeBusStop(busStopId, routeId);
        return ResponseEntity.noContent().build();

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        routeServiceImpl.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
