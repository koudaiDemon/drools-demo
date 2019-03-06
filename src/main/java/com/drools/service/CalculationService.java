package com.drools.service;

import com.drools.fact.order.DiscountValue;
import com.drools.fact.order.Order;
import com.drools.fact.order.OrderEntry;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/3/6  15:02
 */
public class CalculationService {

    public static void calculation(Order order){
        if (null != order) {
            BigDecimal totalPrice = BigDecimal.ZERO;
            final List<OrderEntry> orderEntries = order.getOrderEntries();
            if (CollectionUtils.isNotEmpty(orderEntries)) {
                for (OrderEntry orderEntry : orderEntries) {
                    totalPrice = totalPrice.
                            add(orderEntry.getPrice().multiply(BigDecimal.valueOf(orderEntry.getQuality()))).
                            subtract(calculationDiscount(orderEntry.getDiscountValues()));
                }
            }
            totalPrice = totalPrice.subtract(calculationDiscount(order.getDiscountValues()));
            order.setTotalPrice(totalPrice);
        }
    }

    private static BigDecimal calculationDiscount(final List<DiscountValue> discountValues){
        BigDecimal discount = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(discountValues)) {
            for (DiscountValue discountValue : discountValues) {
                discount = discount.add(discountValue.getDiscount());
            }
        }
        return discount;
    }
}
