package com.drools.fact.order;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author cWww
 * @Title Order
 * @Description 订单
 * @date: 2019/3/6  14:02
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String code;
    private List<OrderEntry> orderEntries;
    private User user;
    private BigDecimal totalPrice;
    private Date creationTime;
    private List<DiscountValue> discountValues;
}
