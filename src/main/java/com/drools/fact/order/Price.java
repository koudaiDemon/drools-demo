package com.drools.fact.order;

import lombok.*;

import java.math.BigDecimal;

/**
 * @author cWww
 * @Title
 * @Description
 * @date: 2019/3/6  14:04
 */
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Price {
    private String code;
    private BigDecimal price;
    private String currency;
}
