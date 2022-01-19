package com.itananina.weblamp.weblamp.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    @Value("#{${discount.dates}}")
    private final Map<String,Integer> discountDaysMap;

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
