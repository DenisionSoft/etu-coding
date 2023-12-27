package com.example.misis2.productclassifier;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.ArrayList;

public interface ProductClassifierRepository extends JpaRepository<ProductClassifier, Integer> {

    @Query(value = "SELECT ins_pc(:r_name, :r_base_ei, :r_parent_id)", nativeQuery = true)
    Integer ins_pc(String r_name, Integer r_base_ei, Integer r_parent_id);

    @Query(value = "SELECT ins_pc_root(:r_name, :r_base_ei)", nativeQuery = true)
    Integer ins_pc_root(String r_name, Integer r_base_ei);

    @Query(value = "SELECT del_pc(:r_id_class)", nativeQuery = true)
    Integer del_pc(Integer r_id_class);

    @Query(value = "SELECT update_pc_name(:r_id_class, :r_name)", nativeQuery = true)
    Integer update_pc_name(Integer r_id_class, String r_name);

    @Query(value = "SELECT update_pc_parent(:r_id_class, :r_parent_id)", nativeQuery = true)
    Integer update_pc_parent(Integer r_id_class, Integer r_parent_id);

    @Query(value = "SELECT update_pc_base_ei(:r_id_class, :r_base_ei)", nativeQuery = true)
    Integer update_pc_base_ei(Integer r_id_class, Integer r_base_ei);

    @Query(value = "SELECT select_pc_children(:r_id_class, :sorttype)", nativeQuery = true)
    ArrayList<?> select_pc_children(Integer r_id_class, String sorttype);

    @Query(value = "SELECT select_pc_children_products(:r_id_class, :sorttype)", nativeQuery = true)
    ArrayList<?> select_pc_children_products(Integer r_id_class, String sorttype);

    @Query(value = "SELECT select_pc_children_only_products(:r_id_class, :sorttype)", nativeQuery = true)
    ArrayList<?> select_pc_children_only_products(Integer r_id_class, String sorttype);

    @Query(value = "SELECT select_pc_parents(:r_id_class, :sorttype)", nativeQuery = true)
    ArrayList<?> select_pc_parents(Integer r_id_class, String sorttype);

    @Procedure(procedureName = "reset_tables")
    void reset_tables();

}
