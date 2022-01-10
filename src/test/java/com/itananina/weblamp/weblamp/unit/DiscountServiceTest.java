package com.itananina.weblamp.weblamp.unit;

import com.itananina.weblamp.weblamp.services.DiscountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

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

        ReflectionTestUtils.setField(discountService, "discountDaysMap", prepareDiscountDaysMap(today,null));
        Assertions.assertEquals(Optional.of(30),discountService.getDiscountForToday());
    }

    @Test
    public void discountDayNotTodayCheck() {
        DiscountService discountService = new DiscountService();

        Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 2);

        ReflectionTestUtils.setField(discountService, "discountDaysMap", prepareDiscountDaysMap(today,null));
        Assertions.assertEquals(Optional.empty(),discountService.getDiscountForToday());
    }

    @Test
    public void discountMidnightRecountCheck() {
        DiscountService discountService = new DiscountService();

        Calendar today = Calendar.getInstance();
        ReflectionTestUtils.setField(discountService, "discountDaysMap", prepareDiscountDaysMap(today,null));
        today.add(Calendar.DATE, -1);
        ReflectionTestUtils.setField(discountService, "today", today); //today устарел
        ReflectionTestUtils.setField(discountService, "discountForToday", Optional.of(1));

        discountService.getDiscountForToday();
        Assertions.assertEquals(Optional.of(30),ReflectionTestUtils.getField(discountService,"discountForToday"));
    }

    @Test
    public void discountNoRecountCheck() {
        DiscountService discountService = new DiscountService();

        Calendar today = Calendar.getInstance();
        ReflectionTestUtils.setField(discountService, "discountDaysMap", prepareDiscountDaysMap(today,null));
        ReflectionTestUtils.setField(discountService, "today", today); //today = сегодня
        ReflectionTestUtils.setField(discountService, "discountForToday", Optional.of(1));

        discountService.getDiscountForToday();
        Assertions.assertEquals(Optional.of(1),ReflectionTestUtils.getField(discountService,"discountForToday"));
    }

    @Test
    public void discountBadValueCheck() {
        DiscountService discountService = new DiscountService();

        Calendar today = Calendar.getInstance();

        ReflectionTestUtils.setField(discountService, "discountForToday", Optional.of(1));
        ReflectionTestUtils.setField(discountService, "discountDaysMap", prepareDiscountDaysMap(today,150));

        discountService.getDiscountForToday();
        Assertions.assertEquals(Optional.empty(),ReflectionTestUtils.getField(discountService,"discountForToday"));
    }

    private Map<String, Integer> prepareDiscountDaysMap(Calendar today, Integer discount) {
        if (discount == null) {
            discount = 30;
        }
        Map<String, Integer> discountDaysMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        String mockDiscountDay = sdf.format(today.getTime());
        discountDaysMap.put(mockDiscountDay, discount);
        return discountDaysMap;
    }
}
