package com.example.servicebackend.controller;

import com.example.servicebackend.constants.Response;
import com.example.servicebackend.dto.actor.Customer;
import com.example.servicebackend.dto.actor.Supplier;
import com.example.servicebackend.dto.actor.User;
import com.example.servicebackend.dto.actor.UserValidationDTO;
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

    @PostMapping("/user")
    public ResponseEntity createUser(@RequestBody User user) {
        Response response = userService.createUser(user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity getUserByUserId(@PathVariable String userId) {
        User user = userService.getUserByUserId(userId);
        return ResponseEntity.ok(user);
    }

    @PatchMapping("/user/validate")
    public ResponseEntity validateUser(@RequestBody User user) {
        UserValidationDTO userValidationDTO = userService.getValidUserByUsernameAndPassword(user.getUserName(),
                user.getPassword());
        return ResponseEntity.ok(userValidationDTO);
    }

    @PatchMapping("/user/customer/{userId}")
    public ResponseEntity updateCustomer(@RequestBody Customer customer) {
        Response response = userService.updateCustomer(customer);
        return ResponseEntity.ok(response);
    }
}
