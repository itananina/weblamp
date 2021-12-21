package com.itananina.weblamp.weblamp.repositories;

import com.itananina.weblamp.weblamp.entities.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    void deleteAllByOrderId(Long id);
}
