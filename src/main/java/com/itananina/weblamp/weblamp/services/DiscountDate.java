package com.itananina.weblamp.weblamp.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class DiscountDate implements Predicate<LocalDate> {

    @Value("#{${discount.dates}}")
    private final Map<String,Integer> discountDaysMap;

    //есть ли today в мапе
    @Override
    public boolean test(LocalDate today) {
        return discountDaysMap.keySet().stream()
                .anyMatch(key -> key.equals(today.format(DateTimeFormatter.ofPattern("MM-dd"))));
    }

}
