package com.itananina.weblamp.weblamp.unit;

import com.itananina.weblamp.weblamp.services.DiscountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


public class DiscountServiceTest {
    @Test
    public void todayIsDiscountDateCheck() {
        DiscountServiceImpl discountService = new DiscountServiceImpl(prepareDiscountDaysMap(LocalDate.now(), 30));
        Assertions.assertTrue(discountService.test(LocalDate.now()));
    }

    @Test
    public void todayIsNotDiscountDateCheck() {
        DiscountServiceImpl discountService = new DiscountServiceImpl(prepareDiscountDaysMap(LocalDate.now().plusDays(1), 30));
        Assertions.assertFalse(discountService.test(LocalDate.now()));
    }

    @Test
    public void badDiscountValueCheck() {
        DiscountServiceImpl discountService = new DiscountServiceImpl(prepareDiscountDaysMap(LocalDate.now(),150));
        Assertions.assertFalse(discountService.test(LocalDate.now()));
    }

    @Test
    public void getDiscountedValueCheck() {
        DiscountServiceImpl discountService = new DiscountServiceImpl(prepareDiscountDaysMap(LocalDate.now(),50));
        Assertions.assertEquals(100, discountService.getDiscountedValue(200));
    }

    @Test
    public void getDiscountedValueForDateCheck() {
        DiscountServiceImpl discountService = new DiscountServiceImpl(prepareDiscountDaysMap(LocalDate.now().plusDays(1),10));
        Assertions.assertEquals(180, discountService.getDiscountedValue(200, LocalDate.now().plusDays(1)));
    }

    private Map<String, Integer> prepareDiscountDaysMap(LocalDate today, int discount) {
        Map<String, Integer> discountDaysMap = new HashMap<>();
        discountDaysMap.put(today.format(DateTimeFormatter.ofPattern("MM-dd")), discount);
        return discountDaysMap;
    }
}
