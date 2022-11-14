package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.model.Address;
import dev.tolstov.buspark.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @GetMapping
    public List<Address> findAll() {
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    public Address findById(@PathVariable Long id) {
        return addressService.findById(id);
    }


    // обработка POST, PUT, DELETE должна быть от родительской
    // entity. т.е. либо Employee, либо BusStop. Иначе половина логики идет куда подальше
//    @PostMapping("employeeHomeAddress")
//    public Address save(@RequestBody Address address) {
//        System.out.println("ADR");
//        return addressService.save(address);
//    }
//
//    @PostMapping("busStopAddress")
//    public Address save(@RequestBody AddressEmployeeDTO addressDTO) {
//        System.out.println("DTO");
//        Address address = new Address();
//        BeanUtils.copyProperties(addressDTO, address);
//        return addressService.save(address);
//    }
//
//    @PutMapping("/employeeHomeAddress/{id}")
//    public Address update(@PathVariable Long id, @RequestBody Address addressInfo) {
//        return addressService.update(id, addressInfo);
//    }
//
//    @PutMapping("/busStopAddress/{id}")
//    public Address update(@PathVariable Long id, @RequestBody AddressEmployeeDTO dto) {
//        return addressService.update(id, dto);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteById(@PathVariable Long id) {
//        addressService.deleteById(id);
//        return ResponseEntity.noContent().build();
//    }
}
