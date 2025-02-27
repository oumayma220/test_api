package com.example.test1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyTechSupplierConfig {

    @Bean
    public CommandLineRunner initMyTechSupplier(SupplierRepository supplierRepository) {
        return args -> {
            if (supplierRepository.findByName("My-Tech").isEmpty()) {
                Supplier myTech = new Supplier();
                myTech.setName("My-Tech");
                myTech.setApiUrl("http://51.77.116.35:8080/my-tech/products");
                myTech.setProductIdPath("$[*].id");
                myTech.setProductNamePath("$[*].product_name");
                myTech.setProductDescPath("$[*].short_summary");
                myTech.setProductUrlPath("$[*].product_url");
                myTech.setProductPricePath("$[*].retail_price");
                myTech.setSupportsPagination(false);

                supplierRepository.save(myTech);
            }
        };
    }
}
