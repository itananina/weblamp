package com.itananina.weblamp.weblamp.converters;

import com.itananina.weblamp.weblamp.dto.*;
import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.entities.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DtoConverter {

    public ProductDto productToProductDto(Product product) {
        return new ProductDto(product.getId(), product.getTitle(), product.getPrice());
    }

    public OrderProductDto opToOrderProductDto(OrderProduct orderProduct) {
        return new OrderProductDto(orderProduct.getId(), orderProduct.getProduct().getTitle(), orderProduct.getProduct().getPrice(), orderProduct.getAmount());
    }

    public OrderDto orderToOrderDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getStatus(),
                order.getOrderProducts().stream()
                    .mapToInt(op->op.getProduct().getPrice()*op.getAmount())
                    .sum(),
                order.getOrderProducts().stream()
                        .map(op->opToOrderProductDto(op))
                        .collect(Collectors.toList()),
                order.getOrderProducts().stream()
                    .mapToInt(op->op.getAmount())
                    .sum());
    }

    public ConfirmedOrderDto orderToConfirmedDto(Order order) {
        return new ConfirmedOrderDto(order.getId(),order.getStatus(),order.getTotal(),
                order.getOrderProducts().stream()
                        .mapToInt(op->op.getAmount())
                        .sum());
    }

    public User jwtRequestToUser(JwtRequest jwtRequest) {
        return new User(jwtRequest.getUsername(), jwtRequest.getPassword(), jwtRequest.getEmail());
    }

}
