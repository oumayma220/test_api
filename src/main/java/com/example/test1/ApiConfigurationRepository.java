package com.example.test1;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiConfigurationRepository extends JpaRepository<ApiConfiguration, Long> {
    Optional<ApiConfiguration> findBySupplier_NameAndApiType(String supplierName, String apiType);

}
