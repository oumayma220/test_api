package com.example.test1;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Data
//@Entity
public class Product {
    //@Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
    //private Long id;

    private String name;
    private String description;
    private String url;
    private Double price;



    public Product(String name, String description, String url, Double price) {
        this.name = name;
        this.description = description;
        this.url = url;
        this.price = price;
    }
}
