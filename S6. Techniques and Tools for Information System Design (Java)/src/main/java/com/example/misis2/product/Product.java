package com.example.misis2.product;

import jakarta.persistence.*;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_product;
    private String name;
    private Integer id_class;

    protected Product() {
    }

    public Product(String name, Integer id_class) {
        this.name = name;
        this.id_class = id_class;
    }

    public Integer getId() {
        return id_product;
    }

    public String getName() {
        return name;
    }

    public Integer getIdClass() {
        return id_class;
    }
}