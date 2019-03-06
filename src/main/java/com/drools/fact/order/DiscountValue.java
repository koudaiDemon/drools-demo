package com.drools.fact.order;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author cWww
 * @Title DiscountValue
 * @Description 折扣类
 * @date: 2019/3/6  14:24
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DiscountValue {
    private String uuid;
    private BigDecimal discount;
}
