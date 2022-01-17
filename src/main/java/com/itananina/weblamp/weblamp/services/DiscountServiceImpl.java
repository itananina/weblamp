package com.itananina.weblamp.weblamp.services;

import com.itananina.weblamp.weblamp.exceptions.UnableToRetrieveDiscountException;
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
public class DiscountServiceImpl {


    private  Optional<Integer> discountForToday;
    private  Calendar today;

    /* ищу в списке скидочных дней сегодняшний день, пытаюсь вернуть размер скидки */
    public Optional<Integer> getDiscountForToday() {

//        if(today!=null && isSameDay(today,Calendar.getInstance())) { // если сегодня уже считали
//            return discountForToday;
//        }
//        log.info("getDiscountForToday got through");
//        today = Calendar.getInstance();
//
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
//
//        discountForToday = discountDaysMap.entrySet().stream()
//            .filter(dayDiscount-> {
//                try {
//                    cal.setTime(sdf.parse(dayDiscount.getKey()));
//                    cal.set(today.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH));
//                    return isSameDay(today, cal);
//                } catch (ParseException e) {
//                    log.error(e.getMessage(), e);
//                }
//                return false;})
//            .map(Map.Entry::getValue)
//            .findFirst();
//
//        try {
//            if (discountForToday.isPresent() && !(discountForToday.get() > 0 && discountForToday.get() < 100)) {
//                discountForToday = Optional.empty();
//                throw new UnableToRetrieveDiscountException("Value for discount is either below 0 or above 100");
//            }
//        } catch(UnableToRetrieveDiscountException e) {
//            log.error(e.getMessage(), e);
//        }

        return null;
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
