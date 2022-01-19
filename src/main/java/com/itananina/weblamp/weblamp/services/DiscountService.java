package com.itananina.weblamp.weblamp.services;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public interface DiscountService {

    int getDiscountedValue(int price, LocalDate date);

    default int getDiscountedValue(int price) {
        return getDiscountedValue(price, LocalDate.now());
    }
}
