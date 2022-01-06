package com.itananina.weblamp.weblamp.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class DiscountService {

    @Value("${discount.date}")
    private Map<String,Integer> discountDaysMap;

    /* ищу в списке скидочных дней сегодняшний день, пытаюсь вернуть размер скидки */
    public Optional<Integer> isDiscountForToday() {
        Calendar today = Calendar.getInstance();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        return discountDaysMap.entrySet().stream()
            .filter(dayDiscount-> {
                try {
                    cal.setTime(sdf.parse(dayDiscount.getKey()));
                    cal.set(today.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
                    return isSameDay(today, cal);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return false;})
        .map(Map.Entry::getValue)
        .findFirst();
    }

    public boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }
}
