package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ErrorMsg;
import com.example.ecommerce.dto.FilterProductDto;
import com.example.ecommerce.dto.ProductDto;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @GetMapping("all")
    public ResponseEntity<List<Product>> getAllProduct(){
        List<Product> products = productRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @PostMapping("filter")
    public ResponseEntity<List<Product>> getFilteredProduct(@RequestBody FilterProductDto filterProductDto){
        List<Product> products = productService.getFilteredProduct(filterProductDto);
        return ResponseEntity.status(HttpStatus.OK).body(products);
    }

    @GetMapping("single/{id}")
    public ResponseEntity<Product> getSingleProduct(@PathVariable int id){
        Product product = productRepository.findById(id).orElse(null);
        if(product == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(product);
        }
        return ResponseEntity.status(HttpStatus.OK).body(product);
    }

    @PostMapping("add")
    public ResponseEntity<String> addProduct(@RequestBody ProductDto productDto){
        productService.addNewProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product Added");
    }
}
