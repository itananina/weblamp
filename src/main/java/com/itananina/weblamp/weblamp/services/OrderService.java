package com.itananina.weblamp.weblamp.services;

import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.exceptions.ResourceNotFoundException;
import com.itananina.weblamp.weblamp.repositories.OrderRepository;
import com.itananina.weblamp.weblamp.services.dictionaries.OrderStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final UserService userService;
    private final DiscountServiceImpl discountService;

    @Transactional
    public Order addProduct(String username, Long productId) {
        Order order = getCurrentOrder(username);
        OrderProduct orderedProduct = order.getOrderProducts().stream()
                .filter(op->op.getProduct().getId().equals(productId))
                .findFirst()
                .orElseGet(()->{
                    OrderProduct op = new OrderProduct(productService.findById(productId),order);
                    order.getOrderProducts().add(op);
                    return op;
                });
        orderedProduct.setPricePerProduct(discountService.getDiscountedValue(orderedProduct.getProduct().getPrice())); //актуализирую цену
        orderedProduct.setAmount(orderedProduct.getAmount()==null ? 1 : orderedProduct.getAmount()+1);
        return order;
    }

    public Order getCurrentOrder(String username) {
        User user = userService.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found by username: "+username));
        return orderRepository.findByUserIdAndStatus(user.getId(), OrderStatus.IN_PROCESS)
                .orElseGet(()-> orderRepository.save(new Order(OrderStatus.IN_PROCESS,user)));
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
            order.getOrderProducts().remove(orderedProduct);
        }
        return order;
    }

    @Transactional
    public Order removeAll(String username) {
        Order order = getCurrentOrder(username);
        order.getOrderProducts().clear();
        return order;
    }

    @Transactional
    public Order confirmOrder(String username) {
        Order order = getCurrentOrder(username);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotal(order.getOrderProducts().stream()
                .mapToInt(op->op.getPricePerProduct()*op.getAmount())
                .sum());
        return order;
    }

    public Page<Order> findAllByUserId(String username, int page) {
        User user = userService.findByUsername(username).orElseThrow(()->new ResourceNotFoundException("User not found by username: "+username));
        return orderRepository.findAllByUserId(user.getId(), PageRequest.of(page-1, 4));
    }

    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order not found by id: "+id));
    }
}
