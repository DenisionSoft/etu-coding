package com.example.misis2.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.ArrayList;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT ins_product(:r_name, :r_id_class)", nativeQuery = true)
    Integer ins_product(String r_name, Integer r_id_class);

    @Query(value = "SELECT del_product(:r_id_product)", nativeQuery = true)
    Integer del_product(Integer r_id_product);

    @Query(value = "SELECT update_product_name(:r_id_product, :r_name)", nativeQuery = true)
    Integer update_product_name(Integer r_id_product, String r_name);

    @Query(value = "SELECT update_product_parent(:r_id_product, :r_id_class)", nativeQuery = true)
    Integer update_product_parent(Integer r_id_product, Integer r_id_class);

    @Query(value = "SELECT select_product_parents(:r_id_product, :sorttype)", nativeQuery = true)
    ArrayList<?> select_product_parents(Integer r_id_product, String sorttype);
}
