package com.example.test1;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String apiUrl;
    private String productIdPath;
    private String productNamePath;
    private String productDescPath;
    private String productUrlPath;
    private String productPricePath;

    private boolean supportsPagination;
    private String pageParamName;
    private String sizeParamName;
}
