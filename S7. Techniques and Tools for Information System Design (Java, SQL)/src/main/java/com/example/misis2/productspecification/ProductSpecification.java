package com.example.misis2.productspecification;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@IdClass(ProductSpecification.class)
@Table(name = "productspecification")
public class ProductSpecification implements Serializable {

    @Id
    private Integer id_product;
    @Id
    private Integer pos_num;
    private Integer id_part;
    private Integer quantity;

    protected ProductSpecification() {}

    public ProductSpecification(Integer id_product, Integer pos_num, Integer id_part, Integer quantity) {
        this.id_product = id_product;
        this.pos_num = pos_num;
        this.id_part = id_part;
        this.quantity = quantity;
    }

    public Integer getIdProduct() {
        return id_product;
    }

    public Integer getPosNum() {
        return pos_num;
    }

    public Integer getIdPart() {
        return id_part;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
