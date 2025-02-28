package com.example.test1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TunisiaNetSupplierConfig {


    @Bean
    public CommandLineRunner initTunisianetSupplier(SupplierRepository supplierRepository,
                                                    ApiConfigurationRepository apiConfigRepo) {
        return args -> {
            Supplier tunisianet = supplierRepository.findByName("Tunisianet")
                    .orElseGet(() -> {
                        Supplier s = new Supplier();
                        s.setName("Tunisianet");
                        return supplierRepository.save(s);
                    });

            if (!apiConfigRepo.findBySupplier_NameAndApiType("Fake-Product", "REST").isPresent()) {
                ApiConfiguration restConfig = new ApiConfiguration();
                restConfig.setApiType("REST");
                restConfig.setApiUrl("http://51.77.116.35:8080/tunisia-net/products");
                restConfig.setProductNamePath("$.content[*].name");
                restConfig.setProductDescPath("$.content[*].description");
                restConfig.setProductUrlPath("$.content[*].url");
                restConfig.setProductPricePath("$.content[*].price");
                restConfig.setSupportsPagination(true);
                restConfig.setPageParamName("page");
                restConfig.setSizeParamName("size");
                restConfig.setSupplier(tunisianet);
                apiConfigRepo.save(restConfig);
            }
        };
    }
}

