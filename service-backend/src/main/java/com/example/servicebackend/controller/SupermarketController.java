package com.example.servicebackend.controller;

import com.example.servicebackend.constants.Response;
import com.example.servicebackend.dto.Supermarket;
import com.example.servicebackend.service.SupermarketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class SupermarketController {

    private final SupermarketService supermarketService;

    @PostMapping("/supermarket")
    public ResponseEntity createSupermarket(@RequestBody Supermarket supermarket) {
        Response response = supermarketService.createNewSupermarket(supermarket);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/supermarket/{id}")
    public ResponseEntity updateSupermarket(@RequestBody Supermarket supermarket,
                                            @PathVariable String id) {
        Response response = supermarketService.updateSupermarket(supermarket, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/supermarket/{id}")
    public ResponseEntity getSupermarketById(@PathVariable String id) {
        Supermarket createdSupermarket = supermarketService.getSupermarketById(id);
        return ResponseEntity.ok(createdSupermarket);
    }

    @GetMapping("/supermarket")
    public ResponseEntity getAllSupermarkets() {
        List<Supermarket> supermarkets = supermarketService.getAllSupermarkets();
        return ResponseEntity.ok(supermarkets);
    }


}
