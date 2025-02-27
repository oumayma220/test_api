package com.example.test1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FakeProductSupplierConfig {

    @Bean
    public CommandLineRunner initFakeProductSupplier(SupplierRepository supplierRepository) {
        return args -> {
            if (supplierRepository.findByName("Fake-Product").isEmpty()) {
                Supplier fakeProduct = new Supplier();
                fakeProduct.setName("Fake-Product");
                fakeProduct.setApiUrl("http://51.77.116.35:8080/fake-product/product");
                fakeProduct.setProductIdPath("$.content[*].id");
                fakeProduct.setProductNamePath("$.content[*].title");
                fakeProduct.setProductDescPath("$.content[*].description");
                fakeProduct.setProductUrlPath("$.content[*].image");
                fakeProduct.setProductPricePath("$.content[*].price");
                fakeProduct.setSupportsPagination(true);
                fakeProduct.setPageParamName("page");
                fakeProduct.setSizeParamName("size");

                supplierRepository.save(fakeProduct);
            }
        };
    }
}
