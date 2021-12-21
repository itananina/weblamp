package com.itananina.weblamp.weblamp.converters;

import com.itananina.weblamp.weblamp.dto.OrderDto;
import com.itananina.weblamp.weblamp.dto.OrderProductDto;
import com.itananina.weblamp.weblamp.entities.Order;
import com.itananina.weblamp.weblamp.entities.OrderProduct;
import com.itananina.weblamp.weblamp.entities.Product;
import com.itananina.weblamp.weblamp.dto.ProductDto;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DtoConverter {

//    public Product dtoToEntity(ProductDto productDto) {
//        return new Product(productDto.getId(), productDto.getTitle(), productDto.getPrice(),null);
//    }

    public ProductDto entityToDto(Product product) {
        return new ProductDto(product.getId(), product.getTitle(), product.getPrice());
    }

    public OrderProductDto opToOrderProductDto(OrderProduct orderProduct) {
        return new OrderProductDto(orderProduct.getId(), orderProduct.getProduct().getTitle(), orderProduct.getProduct().getPrice(), orderProduct.getAmount());
    }

    public OrderDto entityToDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getStatus(),
                order.getOrderProducts().stream()
                    .mapToInt(op->op.getProduct().getPrice())
                    .sum(),
                order.getOrderProducts().stream()
                    .map(op->opToOrderProductDto(op))
                    .collect(Collectors.toList()),
                order.getOrderProducts().stream()
                    .mapToInt(op->op.getAmount())
                    .sum());
    }
}
