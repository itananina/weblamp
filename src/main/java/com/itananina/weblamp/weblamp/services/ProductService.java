package com.itananina.weblamp.weblamp.services;

import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.exceptions.ResourceNotFoundException;
import com.itananina.weblamp.weblamp.repositories.ProductRepository;
import com.itananina.weblamp.weblamp.repositories.specifications.ProductsSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final DiscountService discountService;

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Product not found: "+id));
    }

    public Page<Product> find(Integer page, Integer minPrice, Integer maxPrice, String titlePart) {
        Specification<Product> spec = Specification.where(null); //на самом деле это where true
        if(minPrice!=null) {
            spec = spec.and(ProductsSpecifications.priceGreaterThanOrEqualTo(minPrice));
        }
        if(maxPrice!=null) {
            spec = spec.and(ProductsSpecifications.priceLessThanOrEqualTo(maxPrice));
        }
        if(titlePart!=null) {
            spec = spec.and(ProductsSpecifications.titleLike(titlePart));
        }

        Page<Product> productPage = productRepository.findAll(spec, PageRequest.of(page-1, 4)); //-1 тк для системы с 0, для клиента с 1

        return productPage
                .map(p ->  {
                    p.setPrice(discountService.getDiscountedValue(p.getPrice()));
                    return p;
                });
    }

}
