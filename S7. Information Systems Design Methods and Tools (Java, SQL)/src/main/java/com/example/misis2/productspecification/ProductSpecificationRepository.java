package com.example.misis2.productspecification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface ProductSpecificationRepository extends JpaRepository<ProductSpecification, Integer> {

    @Query(value = "SELECT ins_product_specification(:r_id_product, :r_id_part, :r_quantity)", nativeQuery = true)
    Integer ins_product_specification(Integer r_id_product, Integer r_id_part, Integer r_quantity);

    @Query(value = "SELECT del_product_specification(:r_id_product, :r_pos_num)", nativeQuery = true)
    Integer del_product_specification(Integer r_id_product, Integer r_pos_num);

    @Query(value = "SELECT del_product_specifications_by_product(:r_id_product)", nativeQuery = true)
    Integer del_product_specifications_by_product(Integer r_id_product);

    @Query(value = "SELECT update_product_specification_quantity(:r_id_product, :r_pos_num, :r_quantity)", nativeQuery = true)
    Integer update_product_specification_quantity(Integer r_id_product, Integer r_pos_num, Integer r_quantity);

    @Query(value = "SELECT select_product_specifications_with_names(:r_id_product, :sorttype)", nativeQuery = true)
    ArrayList<?> select_product_specifications_with_names(Integer r_id_product, String sorttype);

    @Query(value = "SELECT count_product_specifications_unique_with_names(:r_id_product, :r_amount)", nativeQuery = true)
    ArrayList<?> count_product_specifications_unique_with_names(Integer r_id_product, Integer r_amount);
}
