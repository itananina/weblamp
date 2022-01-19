package com.itananina.weblamp.weblamp.services.dictionaries;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class OrderStatusMap {

    private static final HashMap<OrderStatus,String> statusMap = new HashMap<>();

    static {
        statusMap.put(OrderStatus.IN_PROCESS,"В процессе");
        statusMap.put(OrderStatus.CONFIRMED,"Оформлен");
    }

    public static Map<OrderStatus,String> getStatusMap() {
        return Collections.unmodifiableMap(statusMap);
    }
}
