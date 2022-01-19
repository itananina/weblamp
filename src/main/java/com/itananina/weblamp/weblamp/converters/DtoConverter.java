package com.itananina.weblamp.weblamp.converters;

import com.itananina.weblamp.weblamp.dto.*;
import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DtoConverter {

    public Page<ProductDto> productPageToProductDtoPage(Page<Product> productPage) {
        return productPage.map(p->productToProductDto(p));
    }

    public ProductDto productToProductDto(Product product) {
        return new ProductDto(product.getId(), product.getTitle(), product.getPrice());
    }

    public OrderProductDto opToOrderProductDto(OrderProduct orderProduct) {
        return new OrderProductDto(orderProduct.getId(), orderProduct.getProduct().getTitle(),
                orderProduct.getPricePerProduct(),
                orderProduct.getAmount());
    }

    public OrderDto orderToOrderDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getStatus(),
                order.getOrderProducts().stream()
                    .mapToInt(op->op.getPricePerProduct()*op.getAmount())
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
                //из-за этого сыпятся селекты:
                order.getOrderProducts().stream()
                        .mapToInt(op->op.getAmount())
                        .sum());
    }

    public User jwtRequestToUser(JwtRequest jwtRequest) {
        return new User(jwtRequest.getUsername(), jwtRequest.getPassword(), jwtRequest.getEmail());
    }

}
