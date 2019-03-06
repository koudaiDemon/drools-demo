package com.drools.fact.order;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author cWww
 * @Title OrderEntry
 * @Description 订单行
 * @date: 2019/3/6  14:02
 */
@Setter
@Getter
@ToString(exclude = {"order"})
@AllArgsConstructor
@NoArgsConstructor
public class OrderEntry {
    private Integer entryNumber;
    private Product product;
    private BigDecimal price;
    private Long quality;
    private Order order;
    private Boolean giveAway;
    private List<DiscountValue> discountValues;
}
