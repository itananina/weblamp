package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.converters.DtoConverter;
import com.itananina.weblamp.weblamp.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderDetailsController {

    private final OrderService orderService;
    private final DtoConverter converter;

    @GetMapping("/{id}")
    public String getOrderDetails(@PathVariable Long id, Model model) {
        model.addAttribute("order", converter.orderToOrderDto(orderService.findById(id)));
        return "order_details";
    }
}
