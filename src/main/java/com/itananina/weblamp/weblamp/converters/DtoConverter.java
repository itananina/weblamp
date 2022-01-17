package com.itananina.weblamp.weblamp.converters;

import com.itananina.weblamp.weblamp.dto.*;
import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.entities.User;
import com.itananina.weblamp.weblamp.services.DiscountServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DtoConverter {

    private final DiscountServiceImpl discountService;

    private Integer getPriceWithDiscountIfPresent(Optional<Integer> discount, Integer price) {
        return discount.map(d -> price*(100-d)/100).orElse(price);
    }

    public Page<ProductDto> productPageToProductDtoPage(Page<Product> productPage) {
        Optional<Integer> discount = discountService.getDiscountForToday();
        return productPage.map(p->productToProductDto(p,discount));
    }

    public ProductDto productToProductDto(Product product, Optional<Integer> discount) {
        return new ProductDto(product.getId(), product.getTitle(),
                getPriceWithDiscountIfPresent(discount, product.getPrice()));
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
