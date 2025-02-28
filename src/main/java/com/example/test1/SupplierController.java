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
    @GetMapping("/rest")
    public List<Product> getProductsFromRest() {
        return productService.fetchProductsFromMyTech();
    }
    @GetMapping("/tunisianet")
    public List<Product> getProductsFromResttunianet() {
        return productService.fetchProductsFromTunisianet();
    }
    @GetMapping("/fakeproduct")
    public List<Product> getProductsFromRestFakeProduct() {
        return productService.fetchProductsFromFakeProduct();
    }
    @GetMapping("/all")
    public List<Product> getallProducts() {
        return productService.fetchAllProducts();
    }


}
