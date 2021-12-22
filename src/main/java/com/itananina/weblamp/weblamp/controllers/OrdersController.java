package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.converters.DtoConverter;
import com.itananina.weblamp.weblamp.dto.ConfirmedOrderDto;
import com.itananina.weblamp.weblamp.dto.OrderDto;
import com.itananina.weblamp.weblamp.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;
    private final DtoConverter converter;

    @GetMapping
    public List<ConfirmedOrderDto> getOrders(Principal principal) {
        return orderService.findAllByUserId(principal.getName()).stream()
                .map(el->converter.orderToConfirmedDto(el))
                .collect(Collectors.toList());
    }

    @GetMapping("/active")
    public OrderDto showOrder(Principal principal) {
        return converter.orderToOrderDto(orderService.getCurrentOrder(principal.getName()));
    }

    @GetMapping("/confirm")
    public ConfirmedOrderDto confirmOrder(Principal principal) {
        return converter.orderToConfirmedDto(orderService.confirmOrder(principal.getName()));
    }

    @GetMapping("/items/{id}")
    public OrderDto addProduct(@PathVariable Long id, Principal principal) {
        return converter.orderToOrderDto(orderService.addProduct(principal.getName(),id));
    }

    @DeleteMapping("/items/{id}")
    public OrderDto removeProduct(@PathVariable Long id, Principal principal) {
        return converter.orderToOrderDto(orderService.removeProduct(principal.getName(),id));
    }

    @DeleteMapping("/items/")
    public OrderDto removeAllProducts(Principal principal) {
        return converter.orderToOrderDto(orderService.removeAll(principal.getName()));
    }
}
