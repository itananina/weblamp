package com.itananina.weblamp.weblamp.unit;

import com.itananina.weblamp.weblamp.services.DiscountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


public class DiscountServiceTest {

    @Test
    public void discountDayTodayCheck() {
        DiscountService discountService = new DiscountService();

        Calendar today = Calendar.getInstance();

        ReflectionTestUtils.setField(discountService, "discountDaysMap", prepareDiscountDaysMap(today));
        Assertions.assertEquals(Optional.of(30),discountService.isDiscountForToday());
    }

    @Test
    public void discountDayNotTodayCheck() {
        DiscountService discountService = new DiscountService();

        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 2);

        ReflectionTestUtils.setField(discountService, "discountDaysMap", prepareDiscountDaysMap(today));
        Assertions.assertEquals(Optional.empty(),discountService.isDiscountForToday());
    }

    private Map<String, Integer> prepareDiscountDaysMap(Calendar today) {
        Map<String, Integer> discountDaysMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String mockDiscountDay = sdf.format(today.getTime());
        discountDaysMap.put(mockDiscountDay, 30);
        return discountDaysMap;
    }
}
