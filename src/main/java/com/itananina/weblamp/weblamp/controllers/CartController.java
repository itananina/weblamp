package com.itananina.weblamp.weblamp.controllers;

import com.itananina.weblamp.weblamp.converters.DtoConverter;
import com.itananina.weblamp.weblamp.services.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final OrderService orderService;
    private final DtoConverter converter;

    @GetMapping
    public String showOrder(Model model, Principal principal) {
        model.addAttribute("order", converter.orderToOrderDto(orderService.getCurrentOrder(principal.getName())));
        return "cart";
    }

    @DeleteMapping("/items/{id}")
    public String removeProduct(@PathVariable Long id, Principal principal) {
        orderService.removeProduct(principal.getName(),id);
        return "redirect:/api/v1/cart/";
    }

    @DeleteMapping("/items")
    public String removeAllProducts(Principal principal) {
        orderService.removeAll(principal.getName());
        return "redirect:/api/v1/cart/";
    }

    @GetMapping("/confirm")
    public String confirmOrder(Principal principal) {
        return "redirect:/api/v1/orders/"+orderService.confirmOrder(principal.getName()).getId();
    }
}
