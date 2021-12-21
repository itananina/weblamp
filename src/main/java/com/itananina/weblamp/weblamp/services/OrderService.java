package com.itananina.weblamp.weblamp.services;

import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
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
    public Order addProduct(String username, Long productId) {
        Order order = getCurrentOrder(username);
        OrderProduct orderedProduct = order.getOrderProducts().stream()
                .filter(op->op.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(()->orderProductRepository.save(new OrderProduct(productService.findById(productId),order)));
        orderedProduct.setAmount(orderedProduct.getAmount()==null ? 1 : orderedProduct.getAmount()+1);
        return order;
    }

    public Order getCurrentOrder(String username) {
        User user = userService.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found by username: "+username));
        Order order = orderRepository.findByUserIdAndStatus(user.getId(),"В процессе")
                .orElseGet(()-> {
                    log.info("reached 5");
                    return orderRepository.save(new Order("В процессе",user));
                });
        return order;
    }

    @Transactional
    public Order removeProduct(String username, Long orderedProductId) {
        Order order = getCurrentOrder(username);
        OrderProduct orderedProduct = order.getOrderProducts().stream()
                .filter(op->op.getId().equals(orderedProductId))
                .findFirst()
                .orElseThrow(()->new ResourceNotFoundException("Product not found in order by id: "+orderedProductId));
        if(orderedProduct.getAmount() > 1) {
            orderedProduct.setAmount(orderedProduct.getAmount()-1);
        } else {
            orderProductRepository.delete(orderedProduct);
            order.getOrderProducts().remove(orderedProduct);
        }
        return order;
    }

    @Transactional
    public Order removeAll(String username) {
        Order order = getCurrentOrder(username);
        order.getOrderProducts().clear();
        orderProductRepository.deleteAllByOrderId(order.getId());
        return order;
    }

}
