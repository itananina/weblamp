package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.converters.DtoConverter;
import com.itananina.weblamp.weblamp.dto.OrderDto;
import com.itananina.weblamp.weblamp.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    private final DtoConverter converter;

    @GetMapping
    public OrderDto showOrder() {
        return converter.entityToDto(orderService.getCurrentOrder(3L));
    }

    @PutMapping("/items/{id}")
    public OrderDto addProduct(@PathVariable Long id) {
        return converter.entityToDto(orderService.addProduct(3L,id));
    }

//    @DeleteMapping("/items/{id}")
//    public OrderDto removeProduct(@PathVariable Long id) {
//        return orderConverter.entityToDto(orderService.removeProduct(id));
//    }
//
//    @DeleteMapping("/items/")
//    public OrderDto removeAllProducts() {
//        return orderConverter.entityToDto(orderService.removeAll());
//    }
}
