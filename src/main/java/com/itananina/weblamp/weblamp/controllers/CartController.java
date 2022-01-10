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

@Controller
@RequestMapping("api/v1/cart/{username}")
@RequiredArgsConstructor
@Slf4j
public class CartController {
    private final OrderService orderService;
    private final DtoConverter converter;

    @GetMapping
    public String showOrder(@PathVariable String username, Model model) {
        model.addAttribute("username",username);
        model.addAttribute("order", converter.orderToOrderDto(orderService.getCurrentOrder(username)));
        return "cart";
    }

    @DeleteMapping("/items/{id}")
    public String removeProduct(@PathVariable String username, @PathVariable Long id, Model model) {
        orderService.removeProduct(username,id);
        return "redirect:/api/v1/cart/"+username;
    }

    @DeleteMapping("/items")
    public String removeAllProducts(@PathVariable String username, Model model) {
        orderService.removeAll(username);
        return "redirect:/api/v1/cart/"+username;
    }

    @GetMapping("/confirm")
    public String confirmOrder(@PathVariable String username, Model model) {
        return "redirect:/api/v1/orders/"+orderService.confirmOrder(username).getId();
    }
}
