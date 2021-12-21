package com.itananina.weblamp.weblamp.services;

import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.exceptions.ResourceNotFoundException;
import com.itananina.weblamp.weblamp.repositories.OrderProductRepository;
import com.itananina.weblamp.weblamp.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final OrderProductRepository orderProductRepository;

    @Transactional
    public Order addProduct(Long userId, Long productId) {
        Order order = getCurrentOrder(userId);
        log.info("reached 3");
        OrderProduct orderedProduct = order.getOrderProducts().stream()
                .filter(op->op.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(()->orderProductRepository.save(new OrderProduct(productService.findById(productId),order)));
        orderedProduct.setAmount(orderedProduct.getAmount()==null ? 1 : orderedProduct.getAmount()+1);
        log.info("reached 4");
        return order;
    }

    public Order getCurrentOrder(Long userId) {
        log.info("reached 1");
        User user = userService.findById(userId).orElseThrow(()->new ResourceNotFoundException("User not found by id: "+userId));
        Order order = orderRepository.findByUserIdAndStatus(userId,"В процессе")
                .orElseGet(()-> {
                    log.info("reached 5");
                    return orderRepository.save(new Order("В процессе",user));
                });
        log.info("reached 2");
        return order;
    }

//    public Order removeProduct(Long productId) {
//        return orderRepository.deleteById(productId);
//    }
//
//    public Order removeAll() {
//        return orderRepository.removeAll();
//    }
}
