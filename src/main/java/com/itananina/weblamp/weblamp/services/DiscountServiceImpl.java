package com.itananina.weblamp.weblamp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Predicate;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService, Predicate<LocalDate> {

    @Value("#{${discount.dates}}")
    private final Map<String,Integer> discountDaysMap;

    //есть ли today в мапе с нормальной скидкой
    @Override
    public boolean test(LocalDate today) {
        return discountDaysMap.entrySet().stream()
                .anyMatch(el -> el.getKey().equals(today.format(DateTimeFormatter.ofPattern("MM-dd")))
                        && el.getValue() > 0 && el.getValue() < 100);
    }

    // ЕСЛИ today есть в мапе с нормальной скидкой ТО возвращаем значение со скидкой ИНАЧЕ без изменений
    @Override
    public int getDiscountedValue(int price, LocalDate today) {
        return discountDaysMap.entrySet().stream()
                .filter(el -> el.getKey().equals(today.format(DateTimeFormatter.ofPattern("MM-dd")))
                        && el.getValue() > 0 && el.getValue() < 100)
                .map(el-> price*(100-el.getValue() )/100)
                .findFirst()
                .orElse(price);
    }
}
