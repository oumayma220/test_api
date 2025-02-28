package com.example.test1;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity

public class ApiConfiguration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String apiType;
    private String apiUrl;
    private String productNamePath;
    private String productDescPath;
    private String productUrlPath;
    private String productPricePath;
    private boolean supportsPagination;
    private String pageParamName;
    private String sizeParamName;
    @ManyToOne
    private Supplier supplier;
}

