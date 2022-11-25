package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.model.Route;
import dev.tolstov.buspark.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/routes")
public class RouteController {
    @Autowired
    RouteService routeService;

    @GetMapping
    public ResponseEntity<List<Route>> findAll() {
        List<Route> routeList = routeService.findAll();
        if (routeList == null || routeList.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(routeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Route> findById(@PathVariable Long id) {
        return ResponseEntity.ok(routeService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Route> save(@RequestBody Route route) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.save(route));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Route> update(@PathVariable Long id, @RequestBody Route routeInfo) {
        return ResponseEntity.ok(routeService.update(id, routeInfo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        routeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
