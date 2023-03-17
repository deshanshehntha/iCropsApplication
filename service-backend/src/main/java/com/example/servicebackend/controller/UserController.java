package com.example.servicebackend.controller;

import com.example.servicebackend.constants.Response;
import com.example.servicebackend.dto.actor.*;
import com.example.servicebackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    @PostMapping("/user/customer")
    public ResponseEntity createCustomer(@RequestBody Customer customer) {
        Response response = userService.createCustomer(customer);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/user/customer/{userId}")
    public ResponseEntity updateCustomer(@PathVariable String userId,
                                         @RequestBody Customer customer) {
        Response response = userService.updateCustomer(customer, userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/customer/{userId}")
    public ResponseEntity getCustomer(@PathVariable String userId) {
        Customer customer = userService.getCustomerByUserId(userId);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/user/supplier")
    public ResponseEntity createSupplier(@RequestBody Supplier supplier) {
        Response response = userService.createSupplier(supplier);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/user/supplier/{userId}")
    public ResponseEntity updateSupplier(@PathVariable String userId,
                                         @RequestBody Supplier supplier) {
        Response response = userService.updateSupplier(supplier, userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/user/manager")
    public ResponseEntity createManager(@RequestBody Manager manager) {
        Response response = userService.createManager(manager);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/user/manager/{userId}")
    public ResponseEntity updateManager(@PathVariable String userId,
                                         @RequestBody Manager manager) {
        Response response = userService.updateManager(manager, userId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/user/validate")
    public ResponseEntity validateUser(@RequestBody User user) {
        UserValidationDTO userValidationDTO = userService.getValidUserByUsernameAndPassword(user.getUserName(),
                user.getPassword());
        return ResponseEntity.ok(userValidationDTO);
    }

}
