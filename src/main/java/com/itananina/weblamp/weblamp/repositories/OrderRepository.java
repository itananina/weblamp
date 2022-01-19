package com.itananina.weblamp.weblamp.repositories;

import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.services.dictionaries.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    Page<Order> findAllByUserId(Long id, Pageable page);


}
