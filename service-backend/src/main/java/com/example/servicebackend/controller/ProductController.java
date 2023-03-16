package com.example.servicebackend.controller;

import com.example.servicebackend.constants.Response;
import com.example.servicebackend.dto.product.Product;
import com.example.servicebackend.service.CSVReaderTest;
import com.example.servicebackend.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;
    private final CSVReaderTest csvReaderTest;

    @PostMapping("/products")
    public ResponseEntity createProduct(@RequestBody Product product) {
        Response response = productService.createProduct(product);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity getProduct(@PathVariable String id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    @GetMapping("/products")
    public ResponseEntity getProductsBySupermarketId(@RequestParam String supermarketId) {
        List<Product> products = productService.getProductBySupermarketId(supermarketId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("products/sync")
    public ResponseEntity synProductDetails() {
        csvReaderTest.syncData();
        return ResponseEntity.ok("Started");
    }
}
