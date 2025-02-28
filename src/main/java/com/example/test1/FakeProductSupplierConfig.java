package com.example.test1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FakeProductSupplierConfig {
    @Bean
    public CommandLineRunner initFakeProductSupplier(SupplierRepository supplierRepository,
                                                    ApiConfigurationRepository apiConfigRepo) {
        return args -> {
            Supplier FakeProduct = supplierRepository.findByName("Fake-Product")
                    .orElseGet(() -> {
                        Supplier s = new Supplier();
                        s.setName("Fake-Product");
                        return supplierRepository.save(s);
                    });

            if (!apiConfigRepo.findBySupplier_NameAndApiType("Fake-Product", "REST").isPresent()) {
                ApiConfiguration restConfig = new ApiConfiguration();
                restConfig.setApiType("REST");
                restConfig.setApiUrl("http://51.77.116.35:8080/fake-product/product");
                restConfig.setProductNamePath("$.content[*].title");
                restConfig.setProductDescPath("$.content[*].description");
                restConfig.setProductUrlPath("$.content[*].image");
                restConfig.setProductPricePath("$.content[*].price");
                restConfig.setSupportsPagination(true);
                restConfig.setPageParamName("page");
                restConfig.setSizeParamName("size");
                restConfig.setSupplier(FakeProduct);
                apiConfigRepo.save(restConfig);
            }
        };
    }
}


