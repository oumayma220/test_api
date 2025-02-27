package com.example.test1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @Autowired
    private SupplierRepository supplierRepository;


@Autowired
private ProductService productService;
@Autowired
    public void setTunisiaNetProductService(ProductService productService) {
        this.productService = productService;
    }



    @GetMapping("/tunisanet")
    public ResponseEntity<List<Product>> getAllProductstunisanet() {
        List<Product> products = productService.fetchAllProductsFromTunisianet();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/fakeproduct")
    public ResponseEntity<List<Product>> getAllProductsFake() {
        List<Product> products = productService.fetchAllProductsFromFakeProduct();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/mytech")
    public ResponseEntity<List<Product>> getAllProductsMyTech() {
        List<Product> products = productService.fetchProductsFromMyTech();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.fetchAllProducts();
        return ResponseEntity.ok(products);
    }



}
