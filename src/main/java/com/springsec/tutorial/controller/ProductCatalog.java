package com.springsec.tutorial.controller;

import com.springsec.tutorial.model.Product;
import com.springsec.tutorial.service.ProductService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductCatalog
{

    @Autowired
    private final ProductService productService;

    @PostMapping("/create/product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product)
    {
            productService.createProductRecord(product);

            return ResponseEntity.ok(product);
    }

    @GetMapping("/get/products")
    public  ResponseEntity<List<Product>> fetchProducts()
    {
        List<Product> output=productService.getAllProducts();

        return ResponseEntity.ok(output);
    }




}
