package com.itananina.weblamp.weblamp.unit;

import com.itananina.weblamp.weblamp.services.DiscountDate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class DiscountDateTest {

    @Test
    public void todayIsDiscountDateCheck() {
        DiscountDate discountDate = new DiscountDate(prepareDiscountDaysMap(LocalDate.now()));
        Assertions.assertTrue(discountDate.test(LocalDate.now()));
    }

    @Test
    public void todayIsNotDiscountDateCheck() {
        DiscountDate discountDate = new DiscountDate(prepareDiscountDaysMap(LocalDate.now().plusDays(1)));
        Assertions.assertFalse(discountDate.test(LocalDate.now()));
    }

    private Map<String, Integer> prepareDiscountDaysMap(LocalDate today) {
        Map<String, Integer> discountDaysMap = new HashMap<>();
        discountDaysMap.put(today.format(DateTimeFormatter.ofPattern("MM-dd")), 30);
        return discountDaysMap;
    }

}
