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
    public List<Route> findAll() {
        return routeService.findAll();
    }

    @GetMapping("/{id}")
    public Route findById(@PathVariable Long id) {
        return routeService.findById(id);
    }

    @PostMapping
    public Route save(@RequestBody Route route) {
        return routeService.save(route);
    }

    @PutMapping("/{id}")
    public Route update(@PathVariable Long id, @RequestBody Route routeInfo) {
        return routeService.update(id, routeInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        routeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
