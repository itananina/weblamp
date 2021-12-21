package com.itananina.weblamp.weblamp.repositories;

import com.itananina.weblamp.weblamp.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByUserId(Long userId);

    Optional<Order> findByUserIdAndStatus(Long userId, String status);
}
