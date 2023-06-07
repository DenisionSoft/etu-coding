package com.example.misis2.productclassifier;

import jakarta.persistence.*;

@Entity
@Table(name = "productclassifier")
public class ProductClassifier {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id_class;
    private String name;
    private Integer base_ei;
    private Integer parent_id;

    protected ProductClassifier() {}

    public ProductClassifier(String name, Integer base_ei, Integer parent_id) {
        this.name = name;
        this.base_ei = base_ei;
        this.parent_id = parent_id;
    }

    public Integer getId() {
        return id_class;
    }

    public String getName() {
        return name;
    }

    public Integer getBaseEI() {
        return base_ei;
    }

    public Integer getParentId() {
        return parent_id;
    }
}
