package com.itananina.weblamp.weblamp.repositories.specifications;

import com.itananina.weblamp.weblamp.entities.Product;
import org.springframework.data.jpa.domain.Specification;

public class ProductsSpecifications {

    public static Specification<Product> titleLike(String titlePart) {
        //root = Product
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), String.format("%%%s%%", titlePart));
    }

    public static Specification<Product> priceLessThanOrEqualTo(Integer maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public static Specification<Product> priceGreaterThanOrEqualTo(Integer minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }
}
