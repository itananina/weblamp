package com.itananina.weblamp.weblamp.repositories;

import com.itananina.weblamp.weblamp.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
    List<Product> findAllByPriceBetween(Integer min, Integer max);
}
