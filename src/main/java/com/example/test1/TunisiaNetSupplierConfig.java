package com.example.test1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TunisiaNetSupplierConfig {

    @Bean
    public CommandLineRunner initTunisiaNetSupplier(SupplierRepository supplierRepository) {
        return args -> {
            if (supplierRepository.findByName("Tunisia-Net").isEmpty()) {
                Supplier tunisiaNet = new Supplier();
                tunisiaNet.setName("Tunisia-Net");
                tunisiaNet.setApiUrl("http://51.77.116.35:8080/tunisia-net/products");
                tunisiaNet.setProductIdPath("$.content[*].id");
                tunisiaNet.setProductNamePath("$.content[*].name");
                tunisiaNet.setProductDescPath("$.content[*].description");
                tunisiaNet.setProductUrlPath("$.content[*].url");
                tunisiaNet.setProductPricePath("$.content[*].price");
                tunisiaNet.setSupportsPagination(true);
                tunisiaNet.setPageParamName("page");
                tunisiaNet.setSizeParamName("size");

                supplierRepository.save(tunisiaNet);
            }
        };
    }

}