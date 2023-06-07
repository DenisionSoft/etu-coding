package com.example.misis2.productclassifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductClassifierService {
    private static ProductClassifierRepository productClassifierRepository;

    @Autowired
    public ProductClassifierService(ProductClassifierRepository productClassifierRepository) {
        this.productClassifierRepository = productClassifierRepository;
    }

    public static Integer ins_pc(String r_name, Integer r_base_ei, Integer r_parent_id) {
        return productClassifierRepository.ins_pc(r_name, r_base_ei, r_parent_id);
    }

    public static Integer ins_pc_root(String r_name, Integer r_base_ei) {
        return productClassifierRepository.ins_pc_root(r_name, r_base_ei);
    }

    public static Integer del_pc(Integer r_id_class) {
        return productClassifierRepository.del_pc(r_id_class);
    }

    public static Integer update_pc_name(Integer r_id_class, String r_name) {
        return productClassifierRepository.update_pc_name(r_id_class, r_name);
    }

    public static Integer update_pc_parent(Integer r_id_class, Integer r_parent_id) {
        return productClassifierRepository.update_pc_parent(r_id_class, r_parent_id);
    }

    public static Integer update_pc_base_ei(Integer r_id_class, Integer r_base_ei) {
        return productClassifierRepository.update_pc_base_ei(r_id_class, r_base_ei);
    }

    public static ProductClassifier select_pc(Integer r_id_class) {
        try {
            ProductClassifier pc = productClassifierRepository.findById(r_id_class).get();
            return pc;
        } catch (Exception e) {
            return new ProductClassifier("null", null, null);
        }
    }

    public static ProductClassifier select_pc_parent(Integer r_id_class) {
        Integer parent_id = productClassifierRepository.findById(r_id_class).get().getParentId();
        if (parent_id == null) {
            return new ProductClassifier("null", null, null);
        }
        return productClassifierRepository.findById(parent_id).get();
    }

    public static ArrayList<?> select_pc_children(Integer r_id_class, String sorttype) {
        try {
            ArrayList<?> pc_children = productClassifierRepository.select_pc_children(r_id_class, sorttype);
            return pc_children;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static ArrayList<?> select_pc_children_products(Integer r_id_class, String sorttype) {
        try {
            ArrayList<?> pc_children_products = productClassifierRepository.select_pc_children_products(r_id_class, sorttype);
            return pc_children_products;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static ArrayList<?> select_pc_children_only_products(Integer r_id_class, String sorttype) {
        try {
            ArrayList<?> pc_children_only_products = productClassifierRepository.select_pc_children_only_products(r_id_class, sorttype);
            return pc_children_only_products;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static ArrayList<?> select_pc_parents(Integer r_id_class, String sorttype) {
        try {
            ArrayList<?> pc_parents = productClassifierRepository.select_pc_parents(r_id_class, sorttype);
            return pc_parents;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static void reset_tables() {
        productClassifierRepository.reset_tables();
    }
}
