package com.example.test1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyTechSupplierConfig {

    @Bean
    public CommandLineRunner initSuppliers(SupplierRepository supplierRepository,
                                           ApiConfigurationRepository apiConfigRepo) {
        return args -> {
            Supplier myTech = supplierRepository.findByName("My-Tech")
                    .orElseGet(() -> {
                        Supplier s = new Supplier();
                        s.setName("My-Tech");
                        return supplierRepository.save(s);
                    });

            if (!apiConfigRepo.findBySupplier_NameAndApiType("My-Tech", "REST").isPresent()) {
                ApiConfiguration restConfig = new ApiConfiguration();
                restConfig.setApiType("REST");
                restConfig.setApiUrl("http://51.77.116.35:8080/my-tech/products");
                // Exemple de mapping JSONPath, adaptez-les à la structure réelle de la réponse
                restConfig.setProductNamePath("$[*].product_name");
                restConfig.setProductDescPath("$[*].short_summary");
                restConfig.setProductUrlPath("$[*].product_url");
                restConfig.setProductPricePath("$[*].retail_price");
                restConfig.setSupplier(myTech);
                apiConfigRepo.save(restConfig);
            }
        };
    }
}
