package com.example.misis2.product;

import com.example.misis2.productclassifier.ProductClassifier;
import com.example.misis2.productclassifier.ProductClassifierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ProductService {
    private static ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public static Integer ins_product(String r_name, Integer r_id_class) {
        return productRepository.ins_product(r_name, r_id_class);
    }

    public static Integer del_product(Integer r_id_product) {
        return productRepository.del_product(r_id_product);
    }

    public static Integer update_product_name(Integer r_id_product, String r_name) {
        return productRepository.update_product_name(r_id_product, r_name);
    }

    public static Integer update_product_parent(Integer r_id_product, Integer r_id_class) {
        return productRepository.update_product_parent(r_id_product, r_id_class);
    }

    public static Product select_product(Integer r_id_product) {
        try {
            Product product = productRepository.findById(r_id_product).get();
            return product;
        } catch (Exception e) {
            return new Product("null", null);
        }
    }

    public static ProductClassifier select_product_parent(Integer r_id_product) {
        Integer parent_id;
        try {
            parent_id = productRepository.findById(r_id_product).get().getIdClass();
            return ProductClassifierService.select_pc(parent_id);
        } catch (Exception e) {
            return new ProductClassifier("null", null, null);
        }
    }

    public static ArrayList<?> select_product_parents(Integer r_id_product, String sorttype) {
        try {
            ArrayList<?> product_parents = productRepository.select_product_parents(r_id_product, sorttype);
            return product_parents;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
