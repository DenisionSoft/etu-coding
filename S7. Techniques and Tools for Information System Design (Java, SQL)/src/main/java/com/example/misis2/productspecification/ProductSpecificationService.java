package com.example.misis2.productspecification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductSpecificationService {
    private static boolean loop_violation = false;
    private static ProductSpecificationRepository productSpecificationRepository;

    @Autowired
    public ProductSpecificationService(ProductSpecificationRepository productSpecificationRepository) {
        this.productSpecificationRepository = productSpecificationRepository;
    }

    private static void dumbcheckloop(Integer r_id_product, Integer r_id_part, Integer r_offender) {
        for (ProductSpecification productSpecification : productSpecificationRepository.findAll()) {
            System.out.println(productSpecification.getIdProduct() + " " + productSpecification.getIdPart());
            if (productSpecification.getIdProduct().equals(r_id_product) && productSpecification.getIdPart().equals(r_offender)) {
                loop_violation = true;
                return;
            }
        }
    }
    private static void checkLoop(Integer r_id_product, Integer r_id_part, Integer r_offender) {
        if (r_id_product.equals(r_id_part)) {
            loop_violation = true;
            return;
        }

        if (loop_violation) {
            return;
        }

        Integer had_parent = 0;
        for (ProductSpecification productSpecification : productSpecificationRepository.findAll()) {
            had_parent = 0;
            if (productSpecification.getIdPart().equals(r_id_product)) {
                had_parent += 1;
                if (productSpecification.getIdProduct().equals(r_offender)) {
                    loop_violation = true;
                    return;
                }
                checkLoop(productSpecification.getIdProduct(), r_id_product, r_offender);
            }
        }

    }

    public static Integer ins_product_specification(Integer r_id_product, Integer r_id_part, Integer r_quantity) {
        dumbcheckloop(r_id_product, r_id_part, r_id_part);
        checkLoop(r_id_product, r_id_part, r_id_part);
        if (loop_violation) {
            loop_violation = false;
            return -3;
        }
        return productSpecificationRepository.ins_product_specification(r_id_product, r_id_part, r_quantity);
    }

    public static Integer del_product_specification(Integer r_id_product, Integer r_pos_num) {
        return productSpecificationRepository.del_product_specification(r_id_product, r_pos_num);
    }

    public static Integer del_product_specifications_by_product(Integer r_id_product) {
        return productSpecificationRepository.del_product_specifications_by_product(r_id_product);
    }

    public static Integer update_product_specification_quantity(Integer r_id_product, Integer r_pos_num, Integer r_quantity) {
        return productSpecificationRepository.update_product_specification_quantity(r_id_product, r_pos_num, r_quantity);
    }

    public static ProductSpecification select_product_specification(Integer r_id_product, Integer r_pos_num) {
        List<ProductSpecification> productSpecifications = productSpecificationRepository.findAll();
        for (ProductSpecification productSpecification : productSpecifications) {
            if (productSpecification.getIdProduct().equals(r_id_product) && productSpecification.getPosNum().equals(r_pos_num)) {
                return productSpecification;
            }
        }
        return new ProductSpecification(null, null, null, null);
    }

    public static ArrayList<?> select_product_specifications_with_names(Integer r_id_product, String sorttype) {
        try {
            ArrayList<?> product_specifications = productSpecificationRepository.select_product_specifications_with_names(r_id_product, sorttype);
            return product_specifications;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public static ArrayList<?> count_product_specifications_unique_with_names(Integer r_id_product, Integer r_amount) {
        try {
            ArrayList<?> product_specifications = productSpecificationRepository.count_product_specifications_unique_with_names(r_id_product, r_amount);
            return product_specifications;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
