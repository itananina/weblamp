package com.itananina.weblamp.weblamp.services;

import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.dto.ProductDto;
import com.itananina.weblamp.weblamp.exceptions.ResourceNotFoundException;
import com.itananina.weblamp.weblamp.repositories.ProductRepository;
import com.itananina.weblamp.weblamp.repositories.specifications.ProductsSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

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
        return productRepository.findAll(spec, PageRequest.of(page-1, 4)); //-1 тк для системы с 0, для клиента с 1
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product update(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId())
                .orElseThrow(()->new ResourceNotFoundException("Cannot update, product not found by id: "+productDto.getId()));
        product.setPrice(productDto.getPrice());
        product.setTitle(productDto.getTitle());
        return product;
    }

}
