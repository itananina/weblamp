package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.converters.DtoConverter;
import com.itananina.weblamp.weblamp.dto.OrderDto;
import com.itananina.weblamp.weblamp.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    private final DtoConverter converter;

    @GetMapping
    public OrderDto showOrder(Principal principal) {
        return converter.entityToDto(orderService.getCurrentOrder(principal.getName()));
    }

    @PutMapping("/items/{id}")
    public OrderDto addProduct(@PathVariable Long id, Principal principal) {
        return converter.entityToDto(orderService.addProduct(principal.getName(),id));
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
